import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}

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
