import Main.system
import Model.Lecture.ScalaDSLs
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import akka.event.{Logging, LoggingAdapter}
import org.scalatest.BeforeAndAfterAll


class ScalaDSLSpec ()
  extends TestKit(ActorSystem("chatBot"))
    with ImplicitSender
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll{

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  val dslActor = system.actorOf(Props[ScalaDSLs], name = "dslActor")

  "The scalaDSL Actor has two Actor children and" must{
    "send back the information matching to keyword internal dsl" in{
      dslActor ! "Internal DSL"
      expectMsg("""
                |Hello
                |Here are the slides to internal DSLs in Scala:
                |https://docs.google.com/presentation/d/17qvdKHoIsxzfbcmF7Ay8VU_NGNayXsZjVsRmu6tcqug/edit#slide=id.g2fb348ba68_0_466
                |Here is the video to internal DSLs in Scala:
                |https://drive.google.com/file/d/1j_3oH5cj8aMKA0OZm6dDFVWVJfDwohyF/view
                |""".stripMargin)
    }
    "send back the information matching to keyword external dsl" in{
      dslActor ! "External DSL"
      expectMsg("""
                  |Hello
                  |Here are the slides to external DSLs in Scala:
                  |https://docs.google.com/presentation/d/1hZ73RtewIFgercCpan2EwT6fAv0ZmmQLMEUb4rUrSp8/edit#slide=id.g2fb348ba68_0_466
                  |Here is the video to external DSLs in Scala:
                  |https://drive.google.com/file/d/1GqdxtvDhzzNSubH42fQKRaPf987VScM1/view
                  |""".stripMargin)
    }
  }
}
