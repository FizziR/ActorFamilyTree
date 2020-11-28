package Controller

import Model.Calculation.CalculationBot
import Model.Message.MessageBot
import akka.actor.{Actor, Props}
import akka.event.Logging

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