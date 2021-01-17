package Model.MessageParser

import akka.http.scaladsl.model.DateTime.fromIsoDateTimeString

import scala.util.parsing.combinator.RegexParsers
import Model.Message

import scala.util.matching.Regex

class MessageParser extends RegexParsers{

  def dateTime: Regex = "[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z".r
  def space: Regex = "/".r
  def userName: Regex = "[a-zA-zäÄöÖüÜß0-9]+(\\s)*[a-zA-zäÄöÖüÜß0-9]*".r
  def message: Regex = ".*".r

  def messageParser: Parser[Message]=
    dateTime ~ space ~ userName ~ space ~ message ^^{
      case dateTime ~ _ ~ userName ~ _ ~ message =>
        {
          val messageParsing: Message = Message(fromIsoDateTimeString(dateTime).get, userName, message)
          messageParsing
        }
    }
}

