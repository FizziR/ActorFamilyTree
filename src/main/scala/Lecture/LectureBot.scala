package Lecture

import akka.actor.{Actor, ActorRef, Props}
import akka.event.{Logging, LoggingAdapter}

class LectureBot extends Actor{
  val log: LoggingAdapter = Logging(context.system, this)
  val scalaBasic1: ActorRef = context.actorOf(Props[ScalaBasic1], name = "basic1")
  val scalaBasic2: ActorRef = context.actorOf(Props[ScalaBasic2], name = "basic2")
  val scalaTests: ActorRef = context.actorOf(Props[ScalaTests], name = "tests")
  val functionalStyleAndMonads: ActorRef = context.actorOf(Props[FunctionalStyleAndMonads], name = "fucntionalStylasAndMonads")
  val scalaDsls : ActorRef = context.actorOf(Props[ScalaDSLs], name = "dsls")
  val scalaActors : ActorRef = context.actorOf(Props[ScalaActors], name = "actors")
  override def receive: Receive = {
    case msg:String if msg.equals("Tests") => testsBot ! msg
    case _ =>
  }
}

class ScalaBasic1 extends Actor{
  val log: LoggingAdapter = Logging(context.system, this)
  var tests: String =
    """
      |Hello
      |Here are the slides to Tests:
      |https://docs.google.com/presentation/d/1RGVkHwgwYRBU4iHI8A6t7O6iMFgJpLafYln2kJAFGjc/edit
      |Here is the video to Tests:
      |https://drive.google.com/file/d/1oAo37XBqSewUicqhBsTQ2tEJ9j094Hoh/view
      |""".stripMargin
  override def receive: Receive = {
    case "Tests" => log.info(tests)
    case _ =>
  }
}

class ScalaBasic2 extends Actor{

}

class ScalaTests extends Actor{

}

class FunctionalStyleAndMonads extends Actor{

}

class ScalaDSLs extends Actor{
  val internalDSL: ActorRef = context.actorOf(Props[InternalDSL], name = "internalDSL")
  val externalDSL: ActorRef = context.actorOf(Props[ExternaDSL], name = "externalDSL")
}

class ScalaActors extends Actor{

}

class InternalDSL extends Actor{

}

class ExternaDSL extends Actor{

}