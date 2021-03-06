package Model.Calculations

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt
import scala.util.Try

class CalculationBot extends Actor{
    val additionBot: ActorRef = context.actorOf(Props[AdditionBot], name = "additionBot")
    val subtractionBot: ActorRef = context.actorOf(Props[SubtractionBot], name = "subtractionBot")
    val multiplicationBot: ActorRef = context.actorOf(Props[MultiplicationBot], name = "multiplicationBot")
    val divisionBot: ActorRef = context.actorOf(Props[DivisionBot], name = "divisionBot")

    implicit val timeout: Timeout = Timeout(1 seconds)

    override def receive: Receive = {
      case msg:String if msg.contains('+') => {
        val future: Future[Any] = additionBot ? msg
        val result: Any = Await.result(future, timeout.duration)
        sender() ! result
      }
      case msg:String if msg.contains('-') => {
        val future: Future[Any] = subtractionBot ? msg
        val result: Any = Await.result(future, timeout.duration)
        sender() ! result
      }
      case msg:String if msg.contains('*') => {
        val future: Future[Any] = multiplicationBot ? msg
        val result: Any = Await.result(future, timeout.duration)
        sender() ! result
      }
      case msg:String if msg.contains('/') => {
        val future: Future[Any] = divisionBot ? msg
        val result: Any = Await.result(future, timeout.duration)
        sender() ! result
      }
      case _ => sender() ! "There is no valid operator found :-("
    }
  }

  class AdditionBot extends Actor {
    override def receive: Receive = {
      case msg:String => {
        val result: Array[String] = "\\+".r.split(msg)

        val numberOne: Option[Double] = Try { result(0).toDouble }.toOption
        val numberTwo: Option[Double] = Try { result(1).toDouble }.toOption
        var resultOne: Double = 0.0
        var resultTwo: Double = 0.0

        if(numberOne != None && numberTwo != None){
          numberOne match {
            case Some(number:Double) => resultOne = number
          }
          numberTwo match {
            case Some(number:Double) => resultTwo = number
          }
          val tmpSum: Double = resultOne + resultTwo
          if(tmpSum == Math.rint(tmpSum)) {
            val addition: String = "Result: " + result(0) + " + " + result(1) + " = " + tmpSum.toInt
            sender() ! addition
          }
          else{
            val addition: String = "Result: " + result(0) + " + " + result(1) + " = " + tmpSum
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
    override def receive: Receive = {
      case msg:String => {
        val result: Array[String] = "\\-".r.split(msg)

        val numberOne: Option[Double] = Try {
          result(0).toDouble
        }.toOption
        val numberTwo: Option[Double] = Try {
          result(1).toDouble
        }.toOption
        var resultOne: Double = 0.0
        var resultTwo: Double = 0.0

        if (numberOne != None && numberTwo != None) {
          numberOne match {
            case Some(number: Double) => resultOne = number
          }
          numberTwo match {
            case Some(number: Double) => resultTwo = number
          }

          val tmpSubtraction: Double = resultOne - resultTwo

          if (tmpSubtraction == Math.rint(tmpSubtraction)) {
            val subtraction: String = "Result: " + result(0) + " - " + result(1) + " = " + tmpSubtraction.toInt
            sender() ! subtraction
          }
          else {
            val subtraction: String = "Result: " + result(0) + " - " + result(1) + " = " + tmpSubtraction
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
    override def receive: Receive = {
      case msg:String => {
        val result: Array[String] = "\\*".r.split(msg)

        val numberOne: Option[Double] = Try { result(0).toDouble }.toOption
        val numberTwo: Option[Double] = Try { result(1).toDouble }.toOption
        var resultOne: Double = 0.0
        var resultTwo: Double = 0.0

        if(numberOne != None && numberTwo != None) {
          numberOne match {
            case Some(number: Double) => resultOne = number
          }
          numberTwo match {
            case Some(number: Double) => resultTwo = number
          }

          val tmpMultiplication: Double = resultOne * resultTwo
          if (tmpMultiplication == Math.rint(tmpMultiplication)) {
            val multiplication: String = "Result: " + result(0) + " * " + result(1) + " = " + tmpMultiplication.toInt
            sender() ! multiplication
          }
          else {
            val multiplication: String = "Result: " + result(0) + " * " + result(1) + " = " + tmpMultiplication
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
    override def receive: Receive = {
      case msg:String => {
        val result: Array[String] = "\\/".r.split(msg)

        val numberOne: Option[Double] = Try { result(0).toDouble }.toOption
        val numberTwo: Option[Double] = Try { result(1).toDouble }.toOption
        var resultOne: Double = 0.0
        var resultTwo: Double = 0.0

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
            val tmpDivision: Double = resultOne / resultTwo
            if (tmpDivision == Math.rint(tmpDivision)) {
              val division: String = "Result: " + result(0) + " / " + result(1) + " = " + tmpDivision.toInt
              sender() ! division
            }
            else {
              val division: String = "Result: " + result(0) + " / " + result(1) + " = " + tmpDivision
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
