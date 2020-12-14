import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}

class ProducerSpec extends AnyWordSpec  with Matchers {

  val feli = new ProducerContent("Feli", 10, 134)
  val sebi = new ProducerContent("Sebi", 4, 20)
  val philip = new ProducerContent("Philip", 5, 30)
  val values = List(feli, sebi, philip)
  val newValue = new ProducerContent("Philip", 3, 14)

  val producer = new Producer
  "In the producer the sumOfWords and sumOfCharacters is added to old values" when{
    "the name of the author is the same as in old list" in{
     // producer.produceInput(newValue)

    }
  }
}
