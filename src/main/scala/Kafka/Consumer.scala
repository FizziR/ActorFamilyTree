package Kafka

import java.util.Properties
import java.util
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}

import scala.collection.JavaConverters._

object Consumer extends App{

  val  props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "Kafka.MessageDeserializer")
  props.put("group.id", "something")

  val consumer: KafkaConsumer[String, ProducerContent] = new KafkaConsumer[String, ProducerContent](props)

  val TOPIC_MESSAGEDATA: String = "messagedata"

  consumer.subscribe(util.Collections.singletonList(TOPIC_MESSAGEDATA))

  while(true){
    println("Polling..")
    val records: ConsumerRecords[String, ProducerContent] = consumer.poll(1000)
    for (record<-records.asScala){
      println(record.value())
    }
  }
}



