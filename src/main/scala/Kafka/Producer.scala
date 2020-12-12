package Kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.::

object Producer extends App{

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  val TOPIC_SUMOFWORDS = "sumofwords"
  val TOPIC_SUMOFCHARACTERS = "sumofcharacters"

  var VALUES: List[(String, List[Int])] = List()


  def produceInput(inputValuePair: ((String, String), String, Int, Int)): Unit= {
    val mapValues = VALUES.toMap
    if( !mapValues.contains(inputValuePair._2)){
      VALUES = VALUES ++ List((inputValuePair._2, List(inputValuePair._3, inputValuePair._4)))
    }
    else{
      VALUES = VALUES.map(valuePair => if(valuePair._1.equals(inputValuePair._1)) (inputValuePair._2, List(inputValuePair._3, inputValuePair._4)) else valuePair)
    }
    VALUES.foreach(i => {
      val sumOfWords = new ProducerRecord(TOPIC_SUMOFWORDS, i._1, s"Sum of words: ${i._2.head}")
      val sumOfCharacters = new ProducerRecord(TOPIC_SUMOFCHARACTERS, i._1, s"Sum of words: ${i._2.tail}")
      Thread.sleep(500)
      producer.send(sumOfWords)
      Thread.sleep(500)
      producer.send(sumOfCharacters)
    })
    producer.close()
  }
}
