import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import akka.http.scaladsl.model.DateTime
import Model.MessageParser.MessageParserModel
import Model.Message
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class MessageParserModelSpec extends AnyWordSpec  with Matchers {

  val parserModel = new MessageParserModel()
  val messageString = "2020-11-29T10:15:49.304Z / Sebastian Kaltenbach / !Calc1234+4321"

  "The MessageParserModel extends MessageParser and converts message String to Message" when{
    "the parser is succesfull" in{
      parserModel.generateMessageFromString(messageString) should be(Some(Message( DateTime(2020, 11, 29, 10, 15, 49), "Sebastian Kaltenbach", "!Calc1234+4321")))
    }

    "the parser is not succesfull" in{
      parserModel.generateMessageFromString("2020-11-29T10:15:49.304Z - Sebastian Kaltenbach - !Calc1234+4321") should be(None)
    }
  }

}
