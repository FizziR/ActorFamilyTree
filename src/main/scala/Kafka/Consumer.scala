package Kafka

import java.util.Properties
import java.util
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}

import scala.collection.JavaConverters._
import io.circe.parser.decode

object Consumer extends App{

  val  props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("group.id", "something")

  val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)

  val TOPIC_METADATA: String = "metadata"

  consumer.subscribe(util.Collections.singletonList(TOPIC_METADATA))

  while(true){
    println("Polling..")
    val records: ConsumerRecords[String, String] = consumer.poll(1000)
    for (record<-records.asScala){
      println("MESSAGE: " + record.topic() + " - " + record.key() + " -> " + record.value())
      parseMessageToTUI(record.value())

    }
  }

  def parseMessageToTUI(jsonString: String): Unit = {
    val jsonAsList: List[(String, Int, Int)] = decode[List[(String, Int, Int)]](jsonString).right.getOrElse(List(("", 0, 0)))
    var metaBoardString: String = "META BOARD\nUser:\tWords:\tCharacters:\n______________________________\n"
    jsonAsList.foreach(content => {
      metaBoardString += content._1 + "\t" + content._2 + "\t" + content._3 + "\n______________________________\n"
    })
    println(metaBoardString)
  }
}



