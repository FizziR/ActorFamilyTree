package Model.Calculation

import akka.actor.Status.Success
import akka.actor.{Actor, Props}
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout
import com.fasterxml.jackson.databind.JsonSerializer

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.util.Try

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
      case _ => sender() ! "There is no valid operator found :-("
    }
  }

  class AdditionBot extends Actor {
    val log = Logging(context.system, this)
    override def receive: Receive = {
      case msg:String => {
        val result = "\\+".r.split(msg)

        val numberOne = Try { result(0).toDouble }.toOption
        val numberTwo = Try { result(1).toDouble }.toOption
        var resultOne = 0.0
        var resultTwo = 0.0

        if(numberOne != None && numberTwo != None){
          numberOne match {
            case Some(number:Double) => resultOne = number
          }
          numberTwo match {
            case Some(number:Double) => resultTwo = number
          }
          val tmpSum = resultOne + resultTwo
          if(tmpSum == Math.rint(tmpSum)) {
            val addition = "Result: " + result(0) + " + " + result(1) + " = " + tmpSum.toInt
            sender() ! addition
          }
          else{
            val addition = "Result: " + result(0) + " + " + result(1) + " = " + tmpSum
            sender() ! addition
          }
        }
        else{
          sender() ! "There are no valid numbers to calculate. Sorry :-("
        }
      }
    }
  }
  class SubtractionBot extends Actor {
    val log = Logging(context.system, this)
    override def receive: Receive = {
      case msg:String => {
        val result = "\\-".r.split(msg)

        val numberOne = Try {
          result(0).toDouble
        }.toOption
        val numberTwo = Try {
          result(1).toDouble
        }.toOption
        var resultOne = 0.0
        var resultTwo = 0.0

        if (numberOne != None && numberTwo != None) {
          numberOne match {
            case Some(number: Double) => resultOne = number
          }
          numberTwo match {
            case Some(number: Double) => resultTwo = number
          }

          val tmpSubtraction = resultOne - resultTwo

          if (tmpSubtraction == Math.rint(tmpSubtraction)) {
            val subtraction = "Result: " + result(0) + " - " + result(1) + " = " + tmpSubtraction.toInt
            sender() ! subtraction
          }
          else {
            val subtraction = "Result: " + result(0) + " - " + result(1) + " = " + tmpSubtraction
            sender() ! subtraction
          }
        }
        else {
          sender() ! "There are no valid numbers to calculate. Sorry :-("
        }
      }
    }
  }
  class MultiplicationBot extends Actor {
    val log = Logging(context.system, this)
    override def receive: Receive = {
      case msg:String => {
        val result = "\\*".r.split(msg)

        val numberOne = Try { result(0).toDouble }.toOption
        val numberTwo = Try { result(1).toDouble }.toOption
        var resultOne = 0.0
        var resultTwo = 0.0

        if(numberOne != None && numberTwo != None) {
          numberOne match {
            case Some(number: Double) => resultOne = number
          }
          numberTwo match {
            case Some(number: Double) => resultTwo = number
          }

          val tmpMultiplication = resultOne * resultTwo
          if (tmpMultiplication == Math.rint(tmpMultiplication)) {
            val multiplication = "Result: " + result(0) + " * " + result(1) + " = " + tmpMultiplication.toInt
            sender() ! multiplication
          }
          else {
            val multiplication = "Result: " + result(0) + " * " + result(1) + " = " + tmpMultiplication
            sender() ! multiplication
          }
        }
        else {
          sender() ! "There are no valid numbers to calculate. Sorry :-("
        }
      }
    }
  }
  class DivisionBot extends Actor {
    val log = Logging(context.system, this)
    override def receive: Receive = {
      case msg:String => {
        val result = "\\/".r.split(msg)

        val numberOne = Try { result(0).toDouble }.toOption
        val numberTwo = Try { result(1).toDouble }.toOption
        var resultOne = 0.0
        var resultTwo = 0.0

        if(numberOne != None && numberTwo != None) {
          numberOne match {
            case Some(number: Double) => resultOne = number
          }
          numberTwo match {
            case Some(number: Double) => resultTwo = number
          }

          if (resultTwo == 0.0) {
            sender() ! "This is not fair ;-)"
          }
          else {
            val tmpDivision = resultOne / resultTwo
            if (tmpDivision == Math.rint(tmpDivision)) {
              val division = "Result: " + result(0) + " / " + result(1) + " = " + tmpDivision.toInt
              sender() ! division
            }
            else {
              val division = "Result: " + result(0) + " / " + result(1) + " = " + tmpDivision
              sender() ! division
            }
          }
        }
        else{
          sender() ! "There are no valid numbers to calculate. Sorry :-("
        }
      }
    }
  }
