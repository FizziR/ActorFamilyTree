package Kafka

import java.util.Properties
import org.apache.kafka.clients.producer.KafkaProducer

class Producer {

  val props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "Kafka.MessageSerializer")

  val producer: KafkaProducer[String, ProducerContent] = new KafkaProducer[String, ProducerContent](props)

  val TOPIC_MESSAGEDATA: String = "messagedata"
  val KEY_MESSAGEDATA: String = "data"

  var VALUES: List[ProducerContent] = List()

  def produceInput(producerInput: ProducerContent): Unit = {
    if(VALUES.filter(content => content.author.equals(producerInput.author)).isEmpty){
      VALUES = VALUES ++ List(producerInput)
    }
    else{
      VALUES = VALUES.map(valuePair => if(valuePair.author.equals(producerInput.author)) ProducerContent(valuePair.author, valuePair.wordCount + producerInput.wordCount, valuePair.characterCount + producerInput.characterCount) else valuePair)
    }
  }
}
