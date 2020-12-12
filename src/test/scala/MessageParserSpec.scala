import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.wordspec.AnyWordSpec

class MessageParserSpec extends AnyWordSpec with Matchers{
  val parser = new MessageParser()
  "A MessageParser" when{
    "regular expression" in{
      "Feli".matches(parser.userName.regex) should be(true)


    }
  }
}
