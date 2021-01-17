import Controller.ChatServer
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.{ActorSystem, Props}
import org.scalatest.BeforeAndAfterAll

class ChatServerSpec ()
  extends TestKit(ActorSystem("chatBot"))
    with ImplicitSender
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll{
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  val chatServer = system.actorOf(Props[ChatServer], name = "chatServer")

  "The chat server Actor has three actor children and" must{
    "return the following" in{
      chatServer ! "Calc4+6"
      expectMsg("Result: 4 + 6 = 10")
    }
    "return the following info" in{
      chatServer ! "ScalaTests"
      expectMsg("""
                  |Hello
                  |Here are the slides to Tests:
                  |https://docs.google.com/presentation/d/1RGVkHwgwYRBU4iHI8A6t7O6iMFgJpLafYln2kJAFGjc/edit
                  |Here is the video to Tests:
                  |https://drive.google.com/file/d/1oAo37XBqSewUicqhBsTQ2tEJ9j094Hoh/view
                  |""".stripMargin)
    }
    "returns an answer" in{
      chatServer ! "HelloFelicitas Maurer"
      expectMsg("Hello Felicitas Maurer! How are you doing?")
    }
  }

}
