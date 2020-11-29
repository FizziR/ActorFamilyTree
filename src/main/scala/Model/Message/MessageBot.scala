package Model.Message

import akka.actor.{Actor, Props}
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt


class MessageBot extends Actor {
  val log = Logging(context.system, this)
  val helloBot = context.actorOf(Props[HelloBot], name = "helloBot")
  val goodbyeBot = context.actorOf(Props[GoodbyeBot], name = "goodbyeBot")
  val pingBot = context.actorOf(Props[PingBot], name = "pingBot")

  implicit val timeout: Timeout = Timeout(2 seconds)

  override def receive: Receive = {
    case msg:String if msg.equals("Hello") => {
      val future = helloBot ? msg
      val result = Await.result(future, timeout.duration)
      sender() ! result
    }
    case msg:String if msg.equals("Goodbye") => {
      val future = goodbyeBot ? msg
      val result = Await.result(future, timeout.duration)
      sender() ! result
    }
    case msg:String if msg.equals("Ping") => {
      val future = pingBot ? msg
      val result = Await.result(future, timeout.duration)
      sender() ! result
    }
    case _ => sender() ! "Sorry - I can't understand your message"
  }
}

class HelloBot extends Actor {
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case "Hello" => sender() ! "Hello! How are you doing?"
    case _ =>
  }
}

class GoodbyeBot extends Actor {
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case "Goodbye" => sender() ! "Is it already time for leaving? Goodbye!"
    case _ =>
  }
}
class PingBot extends Actor {
    override def receive: Receive = {
      case "Ping" => sender() ! "Pong"
      case _ =>
    }
}