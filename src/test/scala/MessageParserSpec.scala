import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.wordspec.AnyWordSpec

class MessageParserSpec extends AnyWordSpec with Matchers{
  val parser = new MessageParser()
  "A MessageParser" when{
    "regular expression" in{
      "Feli".matches(parser.userName.regex) should be(true)

      "Feli_%".matches(parser.userName.regex) should be(false)

      "2020-11-27T19:44:21.742Z".matches(parser.dateTime.regex) should be(true)

      "2020-11-2719:44:21.742Z".matches(parser.dateTime.regex) should be(false)

      "/".matches(parser.space.regex) should be(true)

      "_".matches(parser.space.regex) should be(false)

      "dfhasjkfall 2138947129 &&§(/ß".matches(parser.message.regex) should be(true)

    }
  }
}
