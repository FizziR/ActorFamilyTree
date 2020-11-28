import Calculation.CalculationBot
import Message.MessageBot
import Lecture.LectureBot
import akka.event.Logging
import akka.actor.{Actor, ActorSystem, Props}

import scala.io.StdIn.readLine

/*
    Messages:
      !Hello
      !Goodbye

    Lecture:
      !Basics
      !more Basics
      !Tests
      !Monads
      !Functional Style

    Calculations:
      !Calc1+1
      !Calc1-1
      !Calc1*1
      !Calc1/1
*/


class ChatServer extends Actor {
  val log = Logging(context.system, this)
  val messageBot = context.actorOf(Props[MessageBot], name = "messageBot")
  val calculationBot = context.actorOf(Props[CalculationBot], name = "calculationBot")
  val lectureBot = context.actorOf(Props[LectureBot], name = "lectureBot")
  def receive = {
    case msg:String if msg.charAt(0).equals('!') => {
        val rawString = msg.substring(1)
         rawString match {
           case msg:String if msg.contains("Calc") => calculationBot ! msg.substring(4)
           case msg:String if msg.contains("Tests") => lectureBot ! msg.substring(0)
           case msg:String if msg.contains("Basics") => lectureBot ! msg.substring(0)
           case msg:String if msg.contains("Monads") => lectureBot ! msg.substring(0)
           case msg:String if msg.contains("Functional Style") => lectureBot ! msg.substring(0)
           case msg:String if msg.equals("Internal DSL") => lectureBot ! msg.substring(0)
           case msg:String if msg.equals("External DSL") => lectureBot ! msg.substring(0)
           case msg:String if msg.equals("DSL") => lectureBot ! msg.substring(0)
           case msg:String if msg.equals("Actors") => lectureBot ! msg.substring(0)
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
}