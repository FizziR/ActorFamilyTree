import java.util.Properties

import io.circe.syntax.EncoderOps
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class Producer {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  val TOPIC_METADATA = "metadata"
  val KEY_METADATA = "data"

  var VALUES: List[ProducerContent] = List()

  def produceInput(producerInput: ProducerContent): Unit = {
    if(VALUES.filter(content => content.author.equals(producerInput.author)).isEmpty){
      VALUES = VALUES ++ List(producerInput)
    }
    else{
      VALUES = VALUES.map(valuePair => if(valuePair.author.equals(producerInput.author)) ProducerContent(valuePair.author, valuePair.wordCount + producerInput.wordCount, valuePair.characterCount + producerInput.characterCount) else valuePair)
    }
    val valueList = VALUES.map(content => (content.author, content.wordCount, content.characterCount))
    val metadata = new ProducerRecord(TOPIC_METADATA, KEY_METADATA, valueList.asJson.toString())
    Thread.sleep(500)
    producer.send(metadata)

    //producer.close()
  }
  def createJSON(key: String) = s"""
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
  def getJSONMetaData(producerContent: ProducerContent) = s"""
  {
    "author": "${producerContent.author}",
    "words": ${producerContent.wordCount},
    "characters": ${producerContent.characterCount}
  }
  """.strip()
}
