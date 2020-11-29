import Controller.DiscordBot
import _root_.Controller.Discord.DiscordClient
import akka.actor.{ActorSystem, Props}

object Main{

  val system = ActorSystem("chatBot")
  val discordBot = system.actorOf(Props[DiscordBot], name = "discordBot")

  def main(args: Array[String]) = {
  }
}
