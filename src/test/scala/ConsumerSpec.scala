import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

import Kafka.Consumer

class ConsumerSpec extends AnyWordSpec with Matchers {

  val consumer = Consumer
  val jsonString = s"""
  {
    "author": "Philip",
    "words": 3,
    "characters": 14
  }
  """.strip()

  "The Consumer converts the recieving JsonFile to String" when{
    "the following function is called" in{
      //consumer.parseMessageToTUI(jsonString)

    }
  }

}
