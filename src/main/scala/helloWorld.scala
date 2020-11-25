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
  val messageBot = context.actorOf(Props[MessageBot], name = "messageBot")
  val calculationBot = context.actorOf(Props[CalculationBot], name = "calculationBot")

  def receive = {
    case msg:String if msg.charAt(0).equals('!') => {
        val rawString = msg.substring(1)
         rawString match {
           case msg:String if msg.contains("Calc") => calculationBot ! msg.substring(4)
           case msg:String => messageBot ! msg
           case _ => log.info("No bot implemented yet")
         }
      }
    case _  => log.info("Message not for me")
  }
}
class MessageBot extends Actor {
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case "Hello" => log.info("Hello you. How are you doing?")
    case _ =>
  }
}
class CalculationBot extends Actor{
  val log = Logging(context.system, this)
  val additionBot = context.actorOf(Props[AdditionBot], name = "additionBot")
  val subtractionBot = context.actorOf(Props[SubtractionBot], name = "subtractionBot")
  val multiplicationBot = context.actorOf(Props[MultiplicationBot], name = "multiplicationBot")
  val divisionBot = context.actorOf(Props[DivisionBot], name = "divisionBot")

  override def receive: Receive = {
    case msg:String if msg.contains('+') => additionBot ! msg
    case msg:String if msg.contains('-') => subtractionBot ! msg
    case msg:String if msg.contains('*') => multiplicationBot ! msg
    case msg:String if msg.contains('/') => divisionBot ! msg
    case _ => log.info("No possible operation")
  }
}

class AdditionBot extends Actor {
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case msg:String => {
      val result = "\\+".r.split(msg)
      log.info("Result: " + result(0) + " + " + result(1) + " = " + (result(0).toInt + result(1).toInt))
    }
    case _ => log.info("Not correctly implemented")
  }
}
class SubtractionBot extends Actor {
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case msg:String => {
      val result = "\\-".r.split(msg)
      log.info("Result: " + result(0) + " - " + result(1) + " = " + (result(0).toInt - result(1).toInt))
    }
    case _ => log.info("Not correctly implemented")
  }
}
class MultiplicationBot extends Actor {
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case msg:String => {
      val result = "\\*".r.split(msg)
      log.info("Result: " + result(0) + " * " + result(1) + " = " + (result(0).toInt * result(1).toInt))
    }
    case _ => log.info("Not correctly implemented")
  }
}
class DivisionBot extends Actor {
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case msg:String => {
      val result = "\\/".r.split(msg)
      log.info("Result: " + result(0) + " / " + result(1) + " = " + (result(0).toInt / result(1).toInt))
    }
    case _ => log.info("Not correctly implemented")
  }
}

object Main extends App {
  val system = ActorSystem("chatBot")
  val chatServer = system.actorOf(Props[ChatServer], name = "chatServer")

  chatServer ! "!Hello"
  chatServer ! "Test"
  chatServer ! "!Calc10+13"
  chatServer ! "!Calc15-10"
  chatServer ! "!Calc2*2"
  chatServer ! "!Calc2/2"
}