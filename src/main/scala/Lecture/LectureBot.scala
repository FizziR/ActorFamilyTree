package Lecture

import akka.actor.{Actor, ActorRef, Props}
import akka.event.{Logging, LoggingAdapter}

class LectureBot extends Actor{
  val log: LoggingAdapter = Logging(context.system, this)
  val testsBot: ActorRef = context.actorOf(Props[TestsBot], name = "testsBot")
  override def receive: Receive = {
    case msg:String if msg.equals("Tests") => testsBot ! msg
    case _ =>
  }
}

class TestsBot extends Actor{
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