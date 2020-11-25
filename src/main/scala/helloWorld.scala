import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging
import akka.actor.{ Actor, ActorRef, ActorSystem, PoisonPill, Props }

/*
    1. Actor -> Chatserver => (String Inputs)
    1.1. Valid Handler -> (!msg)
 */


class ChatServer extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case msg:String if(msg.charAt(0).equals('!')) => log.info("Valid Input")
    case _      => log.info("Message not for me")
  }
}

object Main extends App {
  val system = ActorSystem("chatBot")

  val chatServer = system.actorOf(Props[ChatServer], name = "chatServer")
  chatServer ! "!Test"
  chatServer ! "Test"
  chatServer ! "Test!"
}