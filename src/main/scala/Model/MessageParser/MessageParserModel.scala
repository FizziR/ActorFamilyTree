package Model.MessageParser

class MessageParserModel extends MessageParser{
  val failure = "Failure"
  val error = "Error"

  import Model.Message
  def generateMessageFromString(input_message_string: String) : Option[Message] ={
    parse(messageParser, input_message_string) match{
      case Success(value, _) => Some(value)
      case Failure (msg, _) => {
        println(s"$failure: $msg")
        None
      }
      case Error (msg, _)  => {
        println(s"$error: $msg")
        None
      }
    }
  }

}
