package Model.Messages

import akka.actor.{Actor, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class MessageBot extends Actor {
  val helloBot = context.actorOf(Props[HelloBot], name = "helloBot")
  val goodbyeBot = context.actorOf(Props[GoodbyeBot], name = "goodbyeBot")
  val pingBot = context.actorOf(Props[PingBot], name = "pingBot")

  implicit val timeout: Timeout = Timeout(2 seconds)

  override def receive: Receive = {
    case msg:String if msg.contains("Hello") => {
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
  override def receive: Receive = {
    case msg:String => sender() ! s"Hello ${msg.substring(5)}! How are you doing?"
  }
}

class GoodbyeBot extends Actor {
  override def receive: Receive = {
    case "Goodbye" => sender() ! "Is it already time for leaving? Goodbye!"
  }
}
class PingBot extends Actor {
    override def receive: Receive = {
      case "Ping" => sender() ! "Pong"
    }
}