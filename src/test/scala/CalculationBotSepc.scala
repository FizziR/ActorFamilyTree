import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import akka.actor.{Actor, ActorRef, ActorSystem, Props, ActorContext}
import org.scalatest.BeforeAndAfterAll


class CalculationBotSpec()
  extends TestKit(ActorSystem("chatBot"))
    with ImplicitSender
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll{

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  val calculationBot = system.actorOf(Props[CalculationBot], name = "calculationBot")

  "A calculation bot handles mathematical operations and" must{
    "return the answers for the following operators +" in{
      calculationBot ! "4+6"
      expectMsg("Result: 4 + 6 = 10")
      calculationBot ! "4+1.5"
      expectMsg("Result: 4 + 1.5 = 5.5")

      calculationBot ! "4-6"
      expectMsg("Result: 4 - 6 = -2")
      calculationBot ! "4-1.5"
      expectMsg("Result: 4 - 1.5 = 2.5")

      calculationBot ! "4*6"
      expectMsg("Result: 4 * 6 = 24")
      calculationBot ! "1.5*1.5"
      expectMsg("Result: 1.5 * 1.5 = 2.25")

      calculationBot ! "24/6"
      expectMsg("Result: 24 / 6 = 4")
      calculationBot ! "1/2"
      expectMsg("Result: 1 / 2 = 0.5")
    }
  }
}
