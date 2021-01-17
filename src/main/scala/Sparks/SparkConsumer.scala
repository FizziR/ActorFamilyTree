package Sparks

import Kafka.ProducerContent
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{KafkaUtils, _}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, sql}

import scala.collection.mutable

object SparkConsumer {

  var offset: Int = 0
  var oldValuesSumOfMessages: mutable.Map[String, Int] = collection.mutable.Map[String, Int]()
  var oldValueSumOfWords: mutable.Map[String, Int] = collection.mutable.Map[String, Int]()
  var oldValuesSumOfChars: mutable.Map[String, Int] = collection.mutable.Map[String, Int]()
  def main(args: Array[String]) {

    val sparkConfig: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DiscordStream")
    val sparkStreamingContext: StreamingContext = new StreamingContext(sparkConfig, Seconds(1))
    sparkStreamingContext.sparkContext.setLogLevel("ERROR")

    val kafkaConfig: Map[String, Object] = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "Kafka.MessageDeserializer",
      "group.id" -> "something",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics: Array[String] = Array("messagedata")

    val kafkaStream: InputDStream[ConsumerRecord[String, ProducerContent]] = KafkaUtils.createDirectStream[String, ProducerContent](
      sparkStreamingContext,
      LocationStrategies.PreferConsistent,
      Subscribe[String, ProducerContent](topics, kafkaConfig)
    )

    val discordStream: DStream[ProducerContent] = kafkaStream map (streamRawRecord => streamRawRecord.value)

    val discordStream1Second: DStream[ProducerContent] = discordStream.window(Seconds(1))

    val discordData1Second: DStream[(String, Int, Int)] =
      discordStream1Second.map(record =>(record.author, record.wordCount, record.characterCount))

    val spark: SparkSession = SparkSession.builder.config("spark.master", "local").getOrCreate()

    import spark.implicits._

    var dataBase: sql.DataFrame = Seq(("Test", 0, 0)).toDF("Author", "Words", "Character")
    dataBase.createOrReplaceTempView("dataBase")

    val analyzingData: AnalyzingData = new AnalyzingData
    discordData1Second.foreachRDD(rdd =>
    if(rdd.isEmpty()) println("Es gab keine neuen Daten...")
    else {
      dataBase = dataBase.union(rdd.toDF("Author", "Words", "Character"))

      val dataListWithOffset: List[(String, Int, Int)] = rdd.collect().map { case (author, wordCount, characterCount) => (author, wordCount, characterCount) }.toList
      println("Length of dataList with offset: " + dataListWithOffset.length)

      val authorWithCounts: Array[(String, Int)] = analyzingData.getUsersWhoWrote(sparkStreamingContext.sparkContext, dataListWithOffset)
      println("Authors with count: " + authorWithCounts.mkString(""))

      val authorWithSumOfWords: Array[(String, Int)] = analyzingData.getSumOfWords(sparkStreamingContext.sparkContext, dataListWithOffset)
      println("Authors with sum of words: " + authorWithSumOfWords.mkString(""))

      val authorWithSumOfChars: Array[(String, Int)] = analyzingData.getSumOfChars(sparkStreamingContext.sparkContext, dataListWithOffset)
      println("Authors with sum of chars: " + authorWithSumOfChars.mkString(""))

      authorWithCounts.map(authorWithMessages => {
        if (oldValuesSumOfMessages.contains(authorWithMessages._1)) oldValuesSumOfMessages(authorWithMessages._1) += authorWithMessages._2
        else oldValuesSumOfMessages += (authorWithMessages._1 -> authorWithMessages._2)
      })
      authorWithSumOfWords.map(authorWithWords => {
        if (oldValueSumOfWords.contains(authorWithWords._1)) oldValueSumOfWords(authorWithWords._1) += authorWithWords._2
        else oldValueSumOfWords += (authorWithWords._1 -> authorWithWords._2)
      })
      authorWithSumOfChars.map(authorWithChars => {
        if (oldValuesSumOfChars.contains(authorWithChars._1)) oldValuesSumOfChars(authorWithChars._1) += authorWithChars._2
        else oldValuesSumOfChars += (authorWithChars._1 -> authorWithChars._2)
      })

      println("Map Count: " + oldValuesSumOfMessages + "\nMap Words: " + oldValueSumOfWords +
        "\nMap Chars: " + oldValuesSumOfChars)

    })
    sparkStreamingContext.start()
    sparkStreamingContext.awaitTermination()
  }
}
