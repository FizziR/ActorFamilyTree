import java.util.Properties

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
    val metadata = new ProducerRecord(TOPIC_METADATA, KEY_METADATA, createJSON(TOPIC_METADATA))
    Thread.sleep(500)
    producer.send(metadata)

    //producer.close()
  }
  def createJSON(key: String): String ={
    var jsonString= ""
    if(key.equals(TOPIC_METADATA)){
      jsonString = "{\"" + key + "\":[" + createJSONMetaDataArray() + "]}"
    }
    jsonString
  }
  def createJSONMetaDataArray(): String ={
    var arrayString = ""
    VALUES.foreach(content => {
      arrayString += "{\"author\":\"" + content.author + "\",\"words\":" + content.wordCount + "\",\"characters\":" + content.characterCount + "}"
      if( !(content == VALUES.last)){
        arrayString += ","
      }
    })
    arrayString
  }
}
