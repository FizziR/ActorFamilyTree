import Kafka.ProducerContent
import Sparks.AnalyzingData
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{KafkaUtils, _}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.compat.java8.FunctionConverters.enrichAsJavaFunction


object SparkConsumer {

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


    val analyzingData = new AnalyzingData
    discordData1Second.foreachRDD(rdd =>
      if(rdd.isEmpty()) println("Es gab keine neuen Daten...")
      else {

        val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
        import spark.implicits._

        val dataFrame: DataFrame = rdd.toDF()
        dataFrame.createOrReplaceTempView("dataFrame")

        val countDataFrame: DataFrame = spark.sql("select count(*) as total from dataFrame")
        //countDataFrame.show()

        val completeDataFrame = spark.sql("select * from dataFrame")
        //completeDataFrame.show()

        val author = rdd.collect().map { case (author, wordCount, characterCount) => author}
        val wordCount = rdd.collect().map { case (author, wordCount, characterCount) => wordCount}
        val characterCount = rdd.collect().map { case (author, wordCount, characterCount) => characterCount}

        val authorWithCounts = analyzingData.getUsersWhoWrote(sparkStreamingContext.sparkContext, author)
        println(authorWithCounts.mkString(""))
      }

    )

    sparkStreamingContext.start()
    sparkStreamingContext.awaitTermination()



  }

}
