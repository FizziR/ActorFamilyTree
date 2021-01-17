package Controller

import akka.actor.{Actor, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import Model.Messages.MessageBot
import Model.Calculations.CalculationBot
import Model.Lectures.LectureBot

class ChatServer extends Actor {
  val messageBot = context.actorOf(Props[MessageBot], name = "messageBot")
  val calculationBot = context.actorOf(Props[CalculationBot], name = "calculationBot")
  val lectureBot = context.actorOf(Props[LectureBot], name = "lectureBot")
  implicit val timeout: Timeout = Timeout(2 seconds)

  def receive = {
    case msg:String => {
         msg match {
           case msg:String if msg.contains("Calc") => {
             val future = calculationBot ? msg.substring(4)
             val result = Await.result(future, timeout.duration)
             sender() ! result
           }
           case msg:String if msg.contains("Scala") => {
             val future = lectureBot ? msg.substring(5)
             val result = Await.result(future, timeout.duration)
             sender() ! result
           }
           case msg:String => {
             val future = messageBot ? msg
             val result = Await.result(future, timeout.duration)
             sender() ! result
           }
           case _ =>
         }
      }
  }
}