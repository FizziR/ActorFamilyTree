import akka.actor.Actor
import akka.actor.Props
import akka.event.Logging
import akka.actor.{ Actor, ActorRef, ActorSystem, PoisonPill, Props }

class MyActor extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case "test" => log.info("received test")
    case _      => log.info("received unknown message")
  }
}

object Main extends App {
  val system = ActorSystem("pingPong")

  val myActor = system.actorOf(Props[MyActor], name = "myactor")
  myActor ! "test"
  myActor ! "hi"
}