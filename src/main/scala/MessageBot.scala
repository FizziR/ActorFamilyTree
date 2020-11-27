import akka.actor.Actor
import akka.event.Logging

class MessageBot extends Actor {
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case "Hello" => log.info("Hello you. How are you doing?")
    case _ =>
  }
}
