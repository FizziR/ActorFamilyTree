package Lecture

import akka.actor.{Actor, Props}
import akka.event.Logging

class LectureBot extends Actor{
  val log = Logging(context.system, this)
  val testsBot = context.actorOf(Props[TestsBot], name = "testsBot")
  override def receive: Receive = {
    case msg:String if msg.equals("Tests") => testsBot ! msg
    case _ =>
  }
}

class TestsBot extends Actor{
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case "Tests" => log.info("Hello here are the Slides and the Video to Tests")
    case _ =>
  }
}