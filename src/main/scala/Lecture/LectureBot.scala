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
    case msg:String if msg.equals("Basics") => scalaBasic1 ! msg
    case msg:String if msg.equals("more Basics") => scalaBasic2 ! msg
    case msg:String if msg.equals("Tests") => scalaTests ! msg
    case msg:String if msg.equals("Monads") => functionalStyleAndMonads ! msg
    case msg:String if msg.equals("Functional Style") => functionalStyleAndMonads ! msg
    case msg:String if msg.equals("Internal DSL") => scalaDsls ! msg
    case msg:String if msg.equals("External DSL") => scalaDsls ! msg
    case msg:String if msg.equals("DSL") => scalaDsls ! msg
    case msg:String if msg.equals("Actors") => scalaActors ! msg
    case _ =>
  }
}

class ScalaBasic1 extends Actor{
  val log: LoggingAdapter = Logging(context.system, this)
  val basicsOne: String =
    """
      |Hello
      |Here are the slides to Scala Basics:
      |https://docs.google.com/presentation/d/1IgKbtYGHAELm4kC-d-7niIy_yS95L0vJJYD5yJ-YmPs/edit
      |Here is the video to Scala Basics:
      |https://drive.google.com/file/d/1FNExE2C95TTIsSNxkSIxPq76ykkWMMxB/view
      |""".stripMargin
  override def receive: Receive = {
    case "Basics" => log.info(basicsOne)
    case _ =>
  }
}

class ScalaBasic2 extends Actor{
  val log: LoggingAdapter = Logging(context.system, this)
  val basicsTwo: String =
    """
      |Hello
      |Here are the slides to more Scala Basics:
      |https://docs.google.com/presentation/d/15mmHoV-jZPMwNGVFhC3Qtfo5FGDFv6icBmRwjc5gyGk/edit
      |Here is the video to more Scala Basics:
      |https://drive.google.com/file/d/1wM7K7AexBntjsIEAuqjWnvFwwp0s5Vtf/view
      |""".stripMargin
  override def receive: Receive = {
    case "more Basics" => log.info(basicsTwo)
    case _ =>
  }
}

class ScalaTests extends Actor{
  val log: LoggingAdapter = Logging(context.system, this)
  val tests: String =
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

class FunctionalStyleAndMonads extends Actor{
  val log: LoggingAdapter = Logging(context.system, this)
  val functionsAndMonads: String =
    """
      |Hello
      |Here are the slides to Functional Style and Monads:
      |https://docs.google.com/presentation/d/1GgPJALnTOfm8V2QaFSGiVcM28-Tk6vNn0_OdmxK03ug/edit#slide=id.g2fb348ba68_0_466
      |Here is the video to Functional Style and Monads:
      |https://drive.google.com/file/d/17-5iM1nHwiyfxrhIvwXJgaIDYwMQLFak/view
      |""".stripMargin
  override def receive: Receive = {
    case "Monads" => log.info(functionsAndMonads)
    case "Functional Style" => log.info(functionsAndMonads)
    case _ =>
  }
}


class ScalaDSLs extends Actor{
  val log: LoggingAdapter = Logging(context.system, this)
  val internalDSL: ActorRef = context.actorOf(Props[InternalDSL], name = "internalDSL")
  val externalDSL: ActorRef = context.actorOf(Props[ExternalDSL], name = "externalDSL")
  val dsl: String =
    """
      |Hello
      |Here are the slides to internal DSLs in Scala:
      |https://docs.google.com/presentation/d/17qvdKHoIsxzfbcmF7Ay8VU_NGNayXsZjVsRmu6tcqug/edit#slide=id.g2fb348ba68_0_466
      |Here is the video to internal DSLs in Scala:
      |https://drive.google.com/file/d/1j_3oH5cj8aMKA0OZm6dDFVWVJfDwohyF/view
      |Here are the slides to external DSLs in Scala:
      |https://docs.google.com/presentation/d/1hZ73RtewIFgercCpan2EwT6fAv0ZmmQLMEUb4rUrSp8/edit#slide=id.g2fb348ba68_0_466
      |Here is the video to external DSLs in Scala:
      |https://drive.google.com/file/d/1GqdxtvDhzzNSubH42fQKRaPf987VScM1/view
      |""".stripMargin
  override def receive: Receive = {
    case msg:String if msg.equals("Internal DSL") => internalDSL ! msg
    case msg:String if msg.equals("External DSL") => externalDSL ! msg
    case msg:String if msg.equals("DSL") => log.info(dsl)
    case _ =>
  }
}

class ScalaActors extends Actor{
  val log: LoggingAdapter = Logging(context.system, this)
  var actors: String =
    """
      |Hello
      |Here are the slides to Actors in Scala:
      |https://docs.google.com/presentation/d/1xVV8AAPANPfufil4KLIp9uX3g6_tO7se9wGuCU7HT-M/edit#slide=id.g2fb348ba68_0_466
      |Here is the video to Actors in Scala:
      |https://drive.google.com/file/d/1TKm7F44ttuB1wJNbDvE9YzEXeiywjpom/view
      |""".stripMargin
  override def receive: Receive = {
    case "Actors" => log.info(actors)
    case _ =>
  }
}

class InternalDSL extends Actor{
  val log: LoggingAdapter = Logging(context.system, this)
  var internalDSL: String =
    """
      |Hello
      |Here are the slides to internal DSLs in Scala:
      |https://docs.google.com/presentation/d/17qvdKHoIsxzfbcmF7Ay8VU_NGNayXsZjVsRmu6tcqug/edit#slide=id.g2fb348ba68_0_466
      |Here is the video to internal DSLs in Scala:
      |https://drive.google.com/file/d/1j_3oH5cj8aMKA0OZm6dDFVWVJfDwohyF/view
      |""".stripMargin

  override def receive: Receive = {
    case "Internal DSL" => log.info(internalDSL)
    case _ =>
  }
}

class ExternalDSL extends Actor{
  val log: LoggingAdapter = Logging(context.system, this)
  var externalDSL: String =
    """
      |Hello
      |Here are the slides to external DSLs in Scala:
      |https://docs.google.com/presentation/d/1hZ73RtewIFgercCpan2EwT6fAv0ZmmQLMEUb4rUrSp8/edit#slide=id.g2fb348ba68_0_466
      |Here is the video to external DSLs in Scala:
      |https://drive.google.com/file/d/1GqdxtvDhzzNSubH42fQKRaPf987VScM1/view
      |""".stripMargin

  override def receive: Receive = {
    case "External DSL" => log.info(externalDSL)
    case _ =>
  }
}