package Model

import akka.http.scaladsl.model.DateTime
import akka.http.scaladsl.model.DateTime.fromIsoDateTimeString
import scala.util.parsing.combinator.RegexParsers

class MessageParser extends RegexParsers{

  def dateTime = "[0-9\\-]+T[0-9:]+[0-9.]+(Z\\z)".r
  def space = "%"
  def userName = "([a-zA-zäÄöÖüÜß0-9]+\\s*)*".r
  def message = ".*".r

  def messageParser: Parser[Message]=
    dateTime ~ space ~ userName ~ space ~ message ^^{
      case dateTime ~ _ ~ userName ~ _ ~ message =>
        {
          val messageParsing = Message(fromIsoDateTimeString(dateTime).get, userName, message)
          messageParsing
        }
    }




}
