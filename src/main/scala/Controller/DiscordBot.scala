package Controller

import Controller.Discord.DiscordToken
import ackcord.{APIMessage, ClientSettings}
import ackcord.requests.{CreateMessage, CreateMessageData}
import akka.actor.{Actor, Props}
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout
import shapeless.CachedMacros.cache

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, DurationInt}

class DiscordBot extends Actor{
  val log = Logging(context.system, this)
  val chatServer = context.actorOf(Props[ChatServer], name = "chatServer")

  val clientSettings = ClientSettings(DiscordToken().Token)
  val client = Await.result(clientSettings.createClient(), Duration.Inf)

  implicit val timeout: Timeout = Timeout(3 seconds)

  var actorOutput = ""

  client.onEventSideEffects { implicit c => {
    case APIMessage.MessageCreate(_, message, _) => {
      if (message.content.startsWith("!")) {
        self ! message.content
        client.requestsHelper.run(CreateMessage(message.channelId, CreateMessageData(content = actorOutput))
          .map(_ => ()))
        actorOutput = ""
      }
    }
  }
  }
  client.login()
  override def receive: Receive = {
    case msg:String => {
      val future = chatServer ? msg.substring(1)
      val result = Await.result(future, timeout.duration)
      log.info(result.toString)
      actorOutput = result.toString
    }
    case _ =>
  }
}
