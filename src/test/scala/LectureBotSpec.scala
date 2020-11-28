import Main.system
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import akka.event.{Logging, LoggingAdapter}
import org.scalatest.BeforeAndAfterAll


class LectureBotSpec ()
  extends TestKit(ActorSystem("chatBot"))
    with ImplicitSender
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll{



  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  val lectureBot = system.actorOf(Props[LectureBot], name = "lectureBot")

  "The LectrueBot returning Infromation in String" must {

    "send back the string for keyword basic" in {
      lectureBot ! "Basics"
      expectMsg("""
                  |Hello
                  |Here are the slides to Scala Basics:
                  |https://docs.google.com/presentation/d/1IgKbtYGHAELm4kC-d-7niIy_yS95L0vJJYD5yJ-YmPs/edit
                  |Here is the video to Scala Basics:
                  |https://drive.google.com/file/d/1FNExE2C95TTIsSNxkSIxPq76ykkWMMxB/view
                  |""".stripMargin)
    }
    "send back the string for keyword more basics" in{
      lectureBot ! "more Basics"
      expectMsg( """
                   |Hello
                   |Here are the slides to more Scala Basics:
                   |https://docs.google.com/presentation/d/15mmHoV-jZPMwNGVFhC3Qtfo5FGDFv6icBmRwjc5gyGk/edit
                   |Here is the video to more Scala Basics:
                   |https://drive.google.com/file/d/1wM7K7AexBntjsIEAuqjWnvFwwp0s5Vtf/view
                   |""".stripMargin)
    }
    "send back the string for keyword actors" in{
      lectureBot ! "Tests"
      expectMsg("""
                   |Hello
                   |Here are the slides to Tests:
                   |https://docs.google.com/presentation/d/1RGVkHwgwYRBU4iHI8A6t7O6iMFgJpLafYln2kJAFGjc/edit
                   |Here is the video to Tests:
                   |https://drive.google.com/file/d/1oAo37XBqSewUicqhBsTQ2tEJ9j094Hoh/view
                   |""".stripMargin)
    }

  }
}
