import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import Model.ProducerContent
import Kafka.Producer

class ProducerSpec extends AnyWordSpec  with Matchers {

  val feli = ProducerContent("Feli", 10, 134)
  val sebi = ProducerContent("Sebi", 4, 20)
  val philip = ProducerContent("Philip", 5, 30)
  val values = List(feli, sebi, philip)
  val newValue = ProducerContent("Philip", 3, 14)

  val producer = new Producer
  "In the producer the sumOfWords and sumOfCharacters is added to old values" when{
    "the name of the author is the same as in old list" in{
/*      producer.getJSONMetaData(newValue) should be (s"""
  {
    "author": "Philip",
    "words": 3,
    "characters": 14
  }
  """.strip())*/
    }
  }
}
