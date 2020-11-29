import akka.actor.{Actor, Props}
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class CalculationBot extends Actor{
    val log = Logging(context.system, this)
    val additionBot = context.actorOf(Props[AdditionBot], name = "additionBot")
    val subtractionBot = context.actorOf(Props[SubtractionBot], name = "subtractionBot")
    val multiplicationBot = context.actorOf(Props[MultiplicationBot], name = "multiplicationBot")
    val divisionBot = context.actorOf(Props[DivisionBot], name = "divisionBot")

    implicit val timeout: Timeout = Timeout(1 seconds)

    override def receive: Receive = {
      case msg:String if msg.contains('+') => {
        val future = additionBot ? msg
        val result = Await.result(future, timeout.duration)
        sender() ! result
      }
      case msg:String if msg.contains('-') => {
        val future = subtractionBot ? msg
        val result = Await.result(future, timeout.duration)
        sender() ! result
      }
      case msg:String if msg.contains('*') => {
        val future = multiplicationBot ? msg
        val result = Await.result(future, timeout.duration)
        sender() ! result
      }
      case msg:String if msg.contains('/') => {
        val future = divisionBot ? msg
        val result = Await.result(future, timeout.duration)
        sender() ! result
      }
      case _ => log.info("No possible operation")
    }
  }

  class AdditionBot extends Actor {
    val log = Logging(context.system, this)
    override def receive: Receive = {
      case msg:String => {
        val result = "\\+".r.split(msg)
        val addition = "Result: " + result(0) + " + " + result(1) + " = " + (result(0).toInt + result(1).toInt)
        sender() ! addition
      }
      case _ => log.info("Not correctly implemented")
    }
  }
  class SubtractionBot extends Actor {
    val log = Logging(context.system, this)
    override def receive: Receive = {
      case msg:String => {
        val result = "\\-".r.split(msg)
        val subtraction = "Result: " + result(0) + " - " + result(1) + " = " + (result(0).toInt - result(1).toInt)
        sender() ! subtraction
      }
      case _ => log.info("Not correctly implemented")
    }
  }
  class MultiplicationBot extends Actor {
    val log = Logging(context.system, this)
    override def receive: Receive = {
      case msg:String => {
        val result = "\\*".r.split(msg)
        val multiplication = "Result: " + result(0) + " * " + result(1) + " = " + (result(0).toInt * result(1).toInt)
        sender() ! multiplication
      }
      case _ => log.info("Not correctly implemented")
    }
  }
  class DivisionBot extends Actor {
    val log = Logging(context.system, this)
    override def receive: Receive = {
      case msg:String => {
        val result = "\\/".r.split(msg)
        val division = "Result: " + result(0) + " / " + result(1) + " = " + (result(0).toInt / result(1).toInt)
        sender() ! division
      }
      case _ => log.info("Not correctly implemented")
    }
  }
