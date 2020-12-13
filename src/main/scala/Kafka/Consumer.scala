package Kafka

import java.util.Properties
import java.util
import org.apache.kafka.clients.consumer.KafkaConsumer
import scala.collection.JavaConverters._

object Consumer extends App{

  val  props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("group.id", "something")

  val consumer = new KafkaConsumer[String, String](props)

  val TOPIC_SUMOFWORDS = "sumofwords"
  val TOPIC_SUMOFCHARACTERS = "sumofcharacters"

  consumer.subscribe(util.Collections.singletonList(TOPIC_SUMOFWORDS))
  consumer.subscribe(util.Collections.singletonList(TOPIC_SUMOFCHARACTERS))

  while(true){
    println("Polling..")
    val records=consumer.poll(100)
    for (record<-records.asScala){
      println(record.topic() + ": " + record.key() + " - " + record.value())
    }
  }
}

