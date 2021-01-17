import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.{ActorSystem, Props}
import org.scalatest.BeforeAndAfterAll
import Model.Lectures.LectureBot

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
    "send back the string for keyword tests" in{
      lectureBot ! "Tests"
      expectMsg("""
                   |Hello
                   |Here are the slides to Tests:
                   |https://docs.google.com/presentation/d/1RGVkHwgwYRBU4iHI8A6t7O6iMFgJpLafYln2kJAFGjc/edit
                   |Here is the video to Tests:
                   |https://drive.google.com/file/d/1oAo37XBqSewUicqhBsTQ2tEJ9j094Hoh/view
                   |""".stripMargin)
    }
    "send back the string for keyword functional Style" in{
      lectureBot ! "Functional Style"
      expectMsg("""
                  |Hello
                  |Here are the slides to Functional Style and Monads:
                  |https://docs.google.com/presentation/d/1GgPJALnTOfm8V2QaFSGiVcM28-Tk6vNn0_OdmxK03ug/edit#slide=id.g2fb348ba68_0_466
                  |Here is the video to Functional Style and Monads:
                  |https://drive.google.com/file/d/17-5iM1nHwiyfxrhIvwXJgaIDYwMQLFak/view
                  |""".stripMargin)
    }
    "send back the string for keyword Monads" in{
      lectureBot ! "Monads"
      expectMsg("""
                  |Hello
                  |Here are the slides to Functional Style and Monads:
                  |https://docs.google.com/presentation/d/1GgPJALnTOfm8V2QaFSGiVcM28-Tk6vNn0_OdmxK03ug/edit#slide=id.g2fb348ba68_0_466
                  |Here is the video to Functional Style and Monads:
                  |https://drive.google.com/file/d/17-5iM1nHwiyfxrhIvwXJgaIDYwMQLFak/view
                  |""".stripMargin)
    }
    "send back the string for keyword dsl" in{
      lectureBot ! "DSL"
      expectMsg("""
                  |Hello
                  |Here are the slides to internal DSLs in Scala:
                  |https://docs.google.com/presentation/d/17qvdKHoIsxzfbcmF7Ay8VU_NGNayXsZjVsRmu6tcqug/edit#slide=id.g2fb348ba68_0_466
                  |Here is the video to internal DSLs in Scala:
                  |https://drive.google.com/file/d/1j_3oH5cj8aMKA0OZm6dDFVWVJfDwohyF/view
                  |Here are the slides to external DSLs in Scala:
                  |https://docs.google.com/presentation/d/1hZ73RtewIFgercCpan2EwT6fAv0ZmmQLMEUb4rUrSp8/edit#slide=id.g2fb348ba68_0_466
                  |Here is the video to external DSLs in Scala:
                  |https://drive.google.com/file/d/1GqdxtvDhzzNSubH42fQKRaPf987VScM1/view
                  |""".stripMargin)
    }
    "send back the string for keyword internal dsl" in{
      lectureBot ! "Internal DSL"
      expectMsg("""
                  |Hello
                  |Here are the slides to internal DSLs in Scala:
                  |https://docs.google.com/presentation/d/17qvdKHoIsxzfbcmF7Ay8VU_NGNayXsZjVsRmu6tcqug/edit#slide=id.g2fb348ba68_0_466
                  |Here is the video to internal DSLs in Scala:
                  |https://drive.google.com/file/d/1j_3oH5cj8aMKA0OZm6dDFVWVJfDwohyF/view
                  |""".stripMargin)
    }
    "send back the string for keyword external dsl" in{
      lectureBot ! "External DSL"
      expectMsg("""
                  |Hello
                  |Here are the slides to external DSLs in Scala:
                  |https://docs.google.com/presentation/d/1hZ73RtewIFgercCpan2EwT6fAv0ZmmQLMEUb4rUrSp8/edit#slide=id.g2fb348ba68_0_466
                  |Here is the video to external DSLs in Scala:
                  |https://drive.google.com/file/d/1GqdxtvDhzzNSubH42fQKRaPf987VScM1/view
                  |""".stripMargin)
    }
    "send back the string for keyword actors" in{
      lectureBot ! "Actors"
      expectMsg("""
                  |Hello
                  |Here are the slides to Actors in Scala:
                  |https://docs.google.com/presentation/d/1xVV8AAPANPfufil4KLIp9uX3g6_tO7se9wGuCU7HT-M/edit#slide=id.g2fb348ba68_0_466
                  |Here is the video to Actors in Scala:
                  |https://drive.google.com/file/d/1TKm7F44ttuB1wJNbDvE9YzEXeiywjpom/view
                  |""".stripMargin)
    }

  }
}
