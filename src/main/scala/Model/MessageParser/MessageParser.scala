package Model.MessageParser

import akka.http.scaladsl.model.DateTime.fromIsoDateTimeString

import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers

class MessageParser extends RegexParsers{

  def dateTime: Regex = "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z".r
  def space: Regex = "/".r
  def userName: Regex = "[a-zA-zäÄöÖüÜß0-9]+(\\s)*[a-zA-zäÄöÖüÜß0-9]*".r
  def message: Regex = ".*".r

  import Model.Message
  def messageParser: Parser[Message]=
    dateTime ~ space ~ userName ~ space ~ message ^^{
      case dateTime ~ _ ~ userName ~ _ ~ message =>
        {
          val messageParsing = Message(fromIsoDateTimeString(dateTime).get, userName, message)
          messageParsing
        }
    }
}

