import Kafka.ProducerContent
import SparkConsumer.oldValueSumOfWords
import Sparks.AnalyzingData
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.types.{IntegerType, StringType, StructField}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{KafkaUtils, _}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.compat.java8.FunctionConverters.enrichAsJavaFunction


object SparkConsumer {

  var offset = 0
  var oldValuesSumOfMessages = collection.mutable.Map[String, Long]()
  var oldValueSumOfWords  = collection.mutable.Map[String, Long]()
  var oldValuesSumOfChars = collection.mutable.Map[String, Long]()
  def main(args: Array[String]) {

    val sparkConfig = new SparkConf().setMaster("local[*]").setAppName("DiscordStream")
    val sparkStreamingContext = new StreamingContext(sparkConfig, Seconds(1))
    sparkStreamingContext.sparkContext.setLogLevel("ERROR")

    val kafkaConfig = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "Kafka.MessageDeserializer",
      "group.id" -> "something",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    //val kafkaParams=kafkaConfig.asJava
    //val sc = new StreamingContext(spark.sparkContext, Seconds(1))

    val topics = Array("messagedata")

    val kafkaStream: InputDStream[ConsumerRecord[String, ProducerContent]] = KafkaUtils.createDirectStream[String, ProducerContent](
      sparkStreamingContext,
      LocationStrategies.PreferConsistent,
      Subscribe[String, ProducerContent](topics, kafkaConfig)
    )

    val discordStream: DStream[ProducerContent] = kafkaStream map (streamRawRecord => streamRawRecord.value)

    val discordStream1Second: DStream[ProducerContent] = discordStream.window(Seconds(1))

    val discordData1Second: DStream[(String, Int, Int)] =
      discordStream1Second.map(record =>(record.author, record.wordCount, record.characterCount))

    val spark = SparkSession.builder.config("spark.master", "local").getOrCreate()

    import spark.implicits._

    var dataBase = Seq(("Test", 0, 0)).toDF("Author", "Words", "Character")
    dataBase.createOrReplaceTempView("dataBase")

    val analyzingData = new AnalyzingData
    discordData1Second.foreachRDD(rdd =>
    if(rdd.isEmpty()) println("Es gab keine neuen Daten...")
    else {


      dataBase.show()

      //println(rdd.toString())
      dataBase = dataBase.union(rdd.toDF("Author", "Words", "Character"))

      val x = dataBase.collect().length
      println("Length: " + x)



      /*val countDataFrame: DataFrame = spark.sql("select count(*) as total from dataFrame")
        //countDataFrame.show()

      val completeDataFrame = spark.sql("select * from dataFrame")
        completeDataFrame.show()*/
      println("Offset 1: " +offset)
      val dataList = dataBase.collect().map(row => (row.getString(0), row.getInt(1), row.getInt(2))).toList

      val dataListWithOffset= rdd.collect().map { case (author, wordCount, characterCount) => (author, wordCount, characterCount)}.toList
      println("Length of dataList with offset: " +dataListWithOffset.length)

      val authorWithCounts = analyzingData.getUsersWhoWrote(sparkStreamingContext.sparkContext, dataListWithOffset)
        println("Authors with count: "+authorWithCounts.mkString(""))

      val authorWithSumOfWords = analyzingData.getSumOfWords(sparkStreamingContext.sparkContext, dataListWithOffset)
        println("Authors with sum of words: " +authorWithSumOfWords.mkString(""))

      val authorWithSumOfChars = analyzingData.getSumOfChars(sparkStreamingContext.sparkContext, dataListWithOffset)
        println("Authors with sum of chars: " + authorWithSumOfChars.mkString(""))

      offset = x

      authorWithCounts.map(authorWithMessages => {
        if(oldValuesSumOfMessages.contains(authorWithMessages._1)) oldValuesSumOfMessages(authorWithMessages._1)+ authorWithMessages._2
        else oldValuesSumOfMessages ++ authorWithMessages._1 -> authorWithMessages._2
      })
      authorWithSumOfWords.map(authorWithWords => {
        if(oldValueSumOfWords.contains(authorWithWords._1)) oldValueSumOfWords(authorWithWords._1)+ authorWithWords._2
        else oldValueSumOfWords ++ authorWithWords._1 -> authorWithWords._2
      })
      authorWithSumOfChars.map(authorWithChars => {
        if(oldValuesSumOfChars.contains(authorWithChars._1)) oldValuesSumOfChars(authorWithChars._1)+ authorWithChars._2
        else oldValuesSumOfChars ++ authorWithChars._1 -> authorWithChars._2
      })

      println("Offset 2: " + offset)

      println("Map Count: " + oldValuesSumOfMessages + "\nMap Words: " + oldValueSumOfWords +
      "\nMap Chars: " + oldValuesSumOfChars)

      }

    )

    sparkStreamingContext.start()
    sparkStreamingContext.awaitTermination()



  }

}
