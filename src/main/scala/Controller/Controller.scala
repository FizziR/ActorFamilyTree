package Controller

import Controller.Discord.DiscordClient
import Util.Observer
import akka.actor.{ActorSystem, Props}

class Controller(client: DiscordClient) extends Observer{
  client.add(this)

  val system = ActorSystem("chatBot")
  val chatServer = system.actorOf(Props[ChatServer], name = "chatServer")

  override def update: Unit = {
    val msg = client.discordInput
    chatServer ! msg
    client.actorOutput = "Hello From Actors!"
  }
}
