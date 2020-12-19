package Controller

import ackcord.cachehandlers.CacheSnapshotBuilder
import ackcord.data.ChannelId
import ackcord.data.raw.RawMessage
import ackcord.{APIMessage, CacheSnapshot, ClientSettings, MemoryCacheSnapshot, Requests}
import ackcord.requests.{CreateMessage, CreateMessageData, GetChannelMessages, GetChannelMessagesData}
import akka.actor.typed.delivery.internal.ProducerControllerImpl.Request
import akka.actor.{Actor, Props}
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout
import io.circe.Json

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, DurationInt}
import scala.io.Source
import io.circe._
import io.circe.parser._

class DiscordBot extends Actor{
  val log = Logging(context.system, this)
  val chatServer = context.actorOf(Props[ChatServer], name = "chatServer")

  val fileContent = Source.fromFile("Credentials/discordToken.json").getLines().mkString

  val parseResult = parse(fileContent)

  val removeCharacters = "\"".toSet
  val token = parseResult.right.get.\\("token")(0).toString().filterNot(removeCharacters)

  val clientSettings = ClientSettings(token)

  val client = Await.result(clientSettings.createClient(), Duration.Inf)

  implicit val timeout: Timeout = Timeout(3 seconds)

  var actorOutput = ""

  client.onEventSideEffects { implicit c => {
    case APIMessage.MessageCreate(_, message, _) => {
      if (message.content.startsWith("!")) {
        if(message.content.equals("!Hello")){
          self ! message.content + message.authorUsername
        }
        else if(message.content.equals("!Start")){
          val rawMessages = c.getChannelMessages(message.channelId).toList
          val messages = rawMessages.map(i => i._2.content)
          log.info(messages.toString())
        }
        else {
          self ! message.content
        }
        Thread.sleep(500)
        client.requestsHelper.run(CreateMessage(message.channelId, CreateMessageData(content = actorOutput))
          .map(_ => ()))
        if(message.content.equals("!Goodbye")){
          clientSettings.system.terminate()
        }
      }
      else {
        log.info(message.content)
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
