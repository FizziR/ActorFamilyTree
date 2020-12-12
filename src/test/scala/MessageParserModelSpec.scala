import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.wordspec.AnyWordSpec
import akka.http.scaladsl.model.DateTime

class MessageParserModelSpec extends AnyWordSpec  with Matchers {

  val parserModel = new MessageParserModel()
  val messageString = "2020-11-29T10:15:49.304Z _ Sebastian Kaltenbach _ !Calc1234+4321"

  "The MessageParserModel extends MessageParser and converts message String to Message" when{
    "the parser is succesfull" in{
      parserModel.generateMessageFromString(messageString) should be(Some(Message( DateTime(2020, 11, 29, 10, 15, 49), "Sebastian Kaltenbach", "!Calc1234+4321")))
    }
  }

}
