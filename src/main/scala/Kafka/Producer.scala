package Kafka

import java.util.Properties
import io.circe.syntax.EncoderOps
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import Model.ProducerContent

class Producer {

  val props: Properties = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](props)

  val TOPIC_METADATA: String = "metadata"
  val KEY_METADATA: String = "data"

  var VALUES: List[ProducerContent] = List()

  def produceInput(producerInput: ProducerContent): Unit = {
    if(VALUES.filter(content => content.author.equals(producerInput.author)).isEmpty){
      VALUES = VALUES ++ List(producerInput)
    }
    else{
      VALUES = VALUES.map(valuePair => if(valuePair.author.equals(producerInput.author)) ProducerContent(valuePair.author, valuePair.wordCount + producerInput.wordCount, valuePair.characterCount + producerInput.characterCount) else valuePair)
    }
    val valueList: List[(String, Int, Int)] = VALUES.map(content => (content.author, content.wordCount, content.characterCount))
    val metadata: ProducerRecord[String, String] = new ProducerRecord(TOPIC_METADATA, KEY_METADATA, valueList.asJson.toString())
    Thread.sleep(500)
    producer.send(metadata)

    //producer.close()
  }
  def createJSON(key: String): String = s"""
      {
        "$key": [
          ${createJSONMetaDataArray()}
        ]
      }
    """.strip()

  def createJSONMetaDataArray(): String ={
    var arrayString = ""
    VALUES.foreach(content => {
      arrayString += getJSONMetaData(content)
      if( !(content == VALUES.last)){
        arrayString += ","
      }
    })
    arrayString
  }
  def getJSONMetaData(producerContent: ProducerContent): String = s"""
  {
    "author": "${producerContent.author}",
    "words": ${producerContent.wordCount},
    "characters": ${producerContent.characterCount}
  }
  """.strip()
}
