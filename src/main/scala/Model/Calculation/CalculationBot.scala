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

        val numberOne = result(0).toDouble
        val numberTwo = result(1).toDouble
        val tmpSum = numberOne + numberTwo

        if(tmpSum == Math.rint(tmpSum)) {
          val addition = "Result: " + result(0) + " + " + result(1) + " = " + tmpSum.toInt
          sender() ! addition
        }
        else{
          val addition = "Result: " + result(0) + " + " + result(1) + " = " + tmpSum
          sender() ! addition
        }
      }
    }
  }
  class SubtractionBot extends Actor {
    val log = Logging(context.system, this)
    override def receive: Receive = {
      case msg:String => {
        val result = "\\-".r.split(msg)
        val numberOne = result(0).toDouble
        val numberTwo = result(1).toDouble

        val tmpSubtraction = numberOne - numberTwo

        if(tmpSubtraction == Math.rint(tmpSubtraction)) {
          val subtraction = "Result: " + result(0) + " - " + result(1) + " = " + tmpSubtraction.toInt
          sender() ! subtraction
        }
        else{
          val subtraction = "Result: " + result(0) + " - " + result(1) + " = " + tmpSubtraction
          sender() ! subtraction
        }
      }
    }
  }
  class MultiplicationBot extends Actor {
    val log = Logging(context.system, this)
    override def receive: Receive = {
      case msg:String => {
        val result = "\\*".r.split(msg)

        val numberOne = result(0).toDouble
        val numberTwo = result(1).toDouble

        val tmpMultiplication = numberOne * numberTwo
        if(tmpMultiplication == Math.rint(tmpMultiplication)){
          val multiplication = "Result: " + result(0) + " * " + result(1) + " = " + tmpMultiplication.toInt
          sender() ! multiplication
        }
        else{
          val multiplication = "Result: " + result(0) + " * " + result(1) + " = " + tmpMultiplication
          sender() ! multiplication
        }
      }
    }
  }
  class DivisionBot extends Actor {
    val log = Logging(context.system, this)
    override def receive: Receive = {
      case msg:String => {
        val result = "\\/".r.split(msg)

        val numberOne = result(0).toDouble
        val numberTwo = result(1).toDouble

        if(numberTwo == 0.0) {
          sender() ! "This is not fair ;-)"
        }
        else {
          val tmpDivision = numberOne / numberTwo
          if(tmpDivision == Math.rint(tmpDivision)){
            val division = "Result: " + result(0) + " / " + result(1) + " = " + tmpDivision.toInt
            sender() ! division
          }
          else{
            val division = "Result: " + result(0) + " / " + result(1) + " = " + tmpDivision
            sender() ! division
          }
        }
      }
    }
  }
