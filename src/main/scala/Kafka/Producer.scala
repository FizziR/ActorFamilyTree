import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}


class Producer {

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  val TOPIC_SUMOFWORDS = "sumofwords"
  val TOPIC_SUMOFCHARACTERS = "sumofcharacters"

  var VALUES: List[ProducerContent] = List()

  def testProduction(): Unit = {
    val sumOfWords = new ProducerRecord(TOPIC_SUMOFWORDS, "Test Key", s"Sum of words: 5")
    producer.send(sumOfWords)
  }

  def produceInput(producerInput: ProducerContent): Unit = {
    println("IN MESSAGE SEND!")
    if(VALUES.filter(content => content.author.equals(producerInput.author)).isEmpty){
      VALUES = VALUES ++ List(producerInput)
    }
    else{
      VALUES = VALUES.map(valuePair => if(valuePair.author.equals(producerInput.author)) ProducerContent(valuePair.author, valuePair.wordCount + producerInput.wordCount, valuePair.characterCount + producerInput.characterCount) else valuePair)
    }
    println(VALUES)
    VALUES.foreach(content => {
      val sumOfWords = new ProducerRecord(TOPIC_SUMOFWORDS, content.author, s"Sum of words: ${content.wordCount}")
      val sumOfCharacters = new ProducerRecord(TOPIC_SUMOFCHARACTERS, content.author, s"Sum of words: ${content.characterCount}")
      Thread.sleep(500)
      producer.send(sumOfWords)
      Thread.sleep(500)
      producer.send(sumOfCharacters)
    })
    //producer.close()
  }
}
