package Controller

import akka.actor.{Actor, ActorRef, Props}
import akka.event.{Logging, LoggingAdapter}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt
import Model.Messages.MessageBot
import Model.Lectures.LectureBot
import Model.Calculations.CalculationBot

class ChatServer extends Actor {
  val log: LoggingAdapter = Logging(context.system, this)
  val messageBot: ActorRef = context.actorOf(Props[MessageBot], name = "messageBot")
  val calculationBot: ActorRef = context.actorOf(Props[CalculationBot], name = "calculationBot")
  val lectureBot: ActorRef = context.actorOf(Props[LectureBot], name = "lectureBot")
  implicit val timeout: Timeout = Timeout(2 seconds)

  def receive: Receive = {
    case msg:String => {
         msg match {
           case msg:String if msg.contains("Calc") => {
             val future: Future[Any] = calculationBot ? msg.substring(4)
             val result: Any = Await.result(future, timeout.duration)
             sender() ! result
           }
           case msg:String if msg.contains("Scala") => {
             val future: Future[Any] = lectureBot ? msg.substring(5)
             val result: Any = Await.result(future, timeout.duration)
             sender() ! result
           }
           case msg:String => {
             val future: Future[Any] = messageBot ? msg
             val result: Any = Await.result(future, timeout.duration)
             sender() ! result
           }
           case _ => log.info("No bot implemented yet")
         }
      }
  }
}