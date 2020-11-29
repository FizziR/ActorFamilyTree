import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import akka.actor.{Actor, ActorRef, ActorSystem, Props, ActorContext}
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
    /*"return the following" in{
      chatServer ! "!wrk"
      expectMsg("Message not for me")
    }
    "return the following info" in{
      chatServer ! "Tests"
      expectMsg()
    }*/
  }

}
