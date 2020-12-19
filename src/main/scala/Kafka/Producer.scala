import java.util.Properties

import Kafka.ProducerContent
import io.circe.syntax.EncoderOps
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.Future

class Producer {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "Kafka.MessageSerializer")

  val producer = new KafkaProducer[String, ProducerContent](props)

  val TOPIC_MESSAGEDATA = "messagedata"
  val KEY_MESSAGEDATA = "data"

  var VALUES: List[ProducerContent] = List()

  def produceInput(producerInput: ProducerContent): Unit = {
    if(VALUES.filter(content => content.author.equals(producerInput.author)).isEmpty){
      VALUES = VALUES ++ List(producerInput)
    }
    else{
      VALUES = VALUES.map(valuePair => if(valuePair.author.equals(producerInput.author)) new ProducerContent(valuePair.author, valuePair.wordCount + producerInput.wordCount, valuePair.characterCount + producerInput.characterCount) else valuePair)
    }
    val valueList = VALUES.map(content => (content.author, content.wordCount, content.characterCount))
    val message = new ProducerRecord(TOPIC_MESSAGEDATA, KEY_MESSAGEDATA, producerInput)
    val result = producer.send(message)

    //producer.close()
  }
}
