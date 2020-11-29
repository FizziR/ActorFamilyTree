import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import akka.actor.{Actor, ActorRef, ActorSystem, Props, ActorContext}
import org.scalatest.BeforeAndAfterAll


class MessageBotSpec()
  extends TestKit(ActorSystem("chatBot"))
    with ImplicitSender
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll{

    override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
    }

  val messageBot = system.actorOf(Props[MessageBot], name = "messageServer")

  "The message Bot sends messages according to the input he gets and" must{
    "return the following answer for Hello" in{
      messageBot ! "HelloFelicitas Maurer"
      expectMsg("Hello Felicitas Maurer! How are you doing?")
    }
    "return the following answer for Goodbye" in{
      messageBot ! "Goodbye"
      expectMsg("Is it already time for leaving? Goodbye!")
    }
    "return the following answer for Ping" in{
      messageBot ! "Ping"
      expectMsg("Pong")
    }
  }

}
