import Calculation.CalculationBot
import Message.MessageBot
import Lecture.LectureBot
import akka.event.Logging
import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.io.StdIn.readLine
import scala.language.postfixOps

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

  implicit val timeout: Timeout = Timeout(10 seconds)
  //val future = chatServer ? "Hello"
  //val result = Await.result(future, timeout.duration)

  def receive = {
    case msg:String if msg.charAt(0).equals('!') => {
        val rawString = msg.substring(1)
         rawString match {
           case msg:String if msg.contains("Calc") => calculationBot ! msg.substring(4)
           case msg:String if msg.contains("Tests") =>
             val future = lectureBot ? msg.substring(0)
             val result = Await.result(future, timeout.duration)
             log.info(result.toString)
           case msg:String if msg.contains("Basics") => //lectureBot ! msg.substring(0)
             val future = lectureBot ? msg.substring(0)
             val result = Await.result(future, timeout.duration)
             log.info(result.toString)
           case msg:String if msg.contains("Monads") =>
             val future = lectureBot ? msg.substring(0)
             val result = Await.result(future, timeout.duration)
             log.info(result.toString)
           case msg:String if msg.contains("Functional Style") =>
             val future = lectureBot ? msg.substring(0)
             val result = Await.result(future, timeout.duration)
             log.info(result.toString)
           case msg:String if msg.equals("Internal DSL") =>
             val future = lectureBot ? msg.substring(0)
             val result = Await.result(future, timeout.duration)
             log.info(result.toString)
           case msg:String if msg.equals("External DSL") =>
             val future = lectureBot ? msg.substring(0)
             val result = Await.result(future, timeout.duration)
             log.info(result.toString)
           case msg:String if msg.equals("DSL") =>
             val future = lectureBot ? msg.substring(0)
             val result = Await.result(future, timeout.duration)
             log.info(result.toString)
           case msg:String if msg.equals("Actors") =>
             val future = lectureBot ? msg.substring(0)
             val result = Await.result(future, timeout.duration)
             log.info(result.toString)
           case msg:String => messageBot ! msg
           case _ => log.info("No bot implemented yet")
         }
      }
    //case msg:String if msg.contains("Hello") => log.info(msg)
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