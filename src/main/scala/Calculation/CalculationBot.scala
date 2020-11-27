package Calculation

import akka.actor.{Actor, Props}
import akka.event.Logging

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
