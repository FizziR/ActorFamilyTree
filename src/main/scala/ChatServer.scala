import Calculation.CalculationBot
import Message.MessageBot
import Lecture.LectureBot
import akka.event.Logging
import akka.actor.{Actor, ActorSystem, Props}

import scala.io.StdIn.readLine



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

object Main extends App {
  val system = ActorSystem("chatBot")
  val chatServer = system.actorOf(Props[ChatServer], name = "chatServer")

  var input: String = "";

  do{
    input = readLine()
    chatServer ! input
  }while( !input.equals("!Goodbye"))

  /*chatServer ! "!Hello"
  chatServer ! "Test"
  chatServer ! "!Calc10+13"
  chatServer ! "!Calc15-10"
  chatServer ! "!Calc2*2"
  chatServer ! "!Calc2/2"*/
}