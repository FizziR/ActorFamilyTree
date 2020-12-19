package Kafka

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import org.apache.kafka.common.serialization.Deserializer

import scala.util.{Failure, Success, Try}

class MessageDeserializer extends Deserializer[ProducerContent]{
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
  }

  override def deserialize(topic: String, data: Array[Byte]): ProducerContent = {
    lazy val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val tryMessage = Try(mapper.readValue[ProducerContent](data))
    var message = ProducerContent(null, 0, 0)
    tryMessage match {
      case Success(value: ProducerContent) => message = value
      case Failure(exception) =>
    }
    message
  }
}
