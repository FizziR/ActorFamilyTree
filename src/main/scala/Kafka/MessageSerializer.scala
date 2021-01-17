package Kafka

import java.util
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import org.apache.kafka.common.serialization.Serializer

class MessageSerializer extends Serializer[ProducerContent]{
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
  }

  override def serialize(topic: String, data: ProducerContent): Array[Byte] = {
    if(data == null){
      null
    }
    else{
      val objectMapper = new ObjectMapper() with ScalaObjectMapper
      objectMapper.registerModule(DefaultScalaModule)
      println("Serialized: " + objectMapper.writeValueAsString(data))
      objectMapper.writeValueAsString(data).getBytes
    }
  }
}
