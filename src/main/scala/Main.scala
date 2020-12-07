import akka.actor.{ActorSystem, Props}

object Main{

  // Source
  val sourceSystem = ActorSystem("sourceSystem")
  val discordBot = sourceSystem.actorOf(Props[DiscordBot], name = "discordBot")

  def main(args: Array[String]) = {

  }
}
