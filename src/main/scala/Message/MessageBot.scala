package Message

import akka.actor.{Actor, Props}
import akka.event.Logging


class MessageBot extends Actor {
  val log = Logging(context.system, this)
  val helloBot = context.actorOf(Props[HelloBot], name = "helloBot")
  val goodbyeBot = context.actorOf(Props[GoodbyeBot], name = "goodbyeBot")
  override def receive: Receive = {
    case msg:String if msg.equals("Hello") => helloBot ! msg
    case msg:String if msg.equals("Goodbye") => goodbyeBot ! msg
    case _ =>
  }
}

class HelloBot extends Actor {
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case "Hello" => log.info("Hello! How are you doing?")
    case _ =>
  }
}

class GoodbyeBot extends Actor {
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case "Goodbye" => log.info("Is it already time for leaving? Goodbye!")
    case _ =>
  }
}