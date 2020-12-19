
import java.util.Properties
import java.util

import Kafka.ProducerContent
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConverters._
import io.circe.parser.decode

object Consumer extends App{

  val  props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "Kafka.MessageDeserializer")
  props.put("group.id", "something")

  val consumer = new KafkaConsumer[String, ProducerContent](props)

  val TOPIC_MESSAGEDATA = "messagedata"

  consumer.subscribe(util.Collections.singletonList(TOPIC_MESSAGEDATA))

  while(true){
    println("Polling..")
    val records = consumer.poll(1000)
    for (record<-records.asScala){
      //println("MESSAGE: " + record.topic() + " - " + record.key() + " -> " + record.value())
      println(record.value())
    }
  }
}



