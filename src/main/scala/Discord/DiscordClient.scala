package Discord

import ackcord.requests.CreateMessage
import ackcord.{APIMessage, APIMessageCacheUpdate, ClientSettings}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class DiscordClient() {
  val clientSettings = ClientSettings(DiscordToken().Token)

    def login(): Unit ={
      //The client settings contains an excecution context that you can use before you have access to the client
      //import clientSettings.executionContext

      //In real code, please dont block on the client construction
      val client = Await.result(clientSettings.createClient(), Duration.Inf)

      //The client also contains an execution context
      //import client.executionContext
      client.onEventSideEffects { implicit c => {
        case APIMessage.MessageCreate(_, message, _) => {
          println(message.content)
        }
      }
      }
      client.login()
    }
  def logout(): Unit ={
    clientSettings.system.terminate()
  }
}
