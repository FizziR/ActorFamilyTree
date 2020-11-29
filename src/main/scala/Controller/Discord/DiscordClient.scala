package Controller.Discord

import Util.Observable
import ackcord.EventListenerMessage.findCache
import ackcord.commands.CommandMessage.findCache
import ackcord.data.TextChannel
import ackcord.requests.{CreateMessage, CreateMessageData}
import ackcord.{APIMessage, CacheSnapshot, ClientSettings, data}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class DiscordClient extends Observable{

  val clientSettings = ClientSettings(DiscordToken().Token)
  val client = Await.result(clientSettings.createClient(), Duration.Inf)

  var discordInput = ""
  var actorOutput = ""

    def login(): Unit ={
      //The client settings contains an excecution context that you can use before you have access to the client
      //import clientSettings.executionContext

      client.onEventSideEffects { implicit c => {
        case APIMessage.MessageCreate(_, message, _) => {
          discordInput = message.content
          if (message.content.startsWith("!")) {
          notifyObservers
          client.requestsHelper.run(CreateMessage(message.channelId, CreateMessageData(content = actorOutput))
            .map(_ => ()))
          if (message.content.equals("!Goodbye")) {
            logout()
          }
        }
      }
      }
      }
      client.login()
    }
    def logout(): Unit ={
      clientSettings.system.terminate()
    }
}
