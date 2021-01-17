package Controller

import java.io.{BufferedWriter, File, FileWriter}
import ackcord.data.raw.RawMessage
import ackcord.{APIMessage, ClientSettings, DiscordClient, data}
import ackcord.requests.{CreateMessage, CreateMessageData, GetChannelMessages, GetChannelMessagesData}
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import io.circe.{Json, ParsingFailure}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.{Duration, DurationInt}
import io.circe.parser._

import scala.collection.mutable.ListBuffer

class DiscordBot extends Actor {
  val chatServer: ActorRef = context.actorOf(Props[ChatServer], name = "chatServer")

  val fileContent: String = scala.io.Source.fromFile("Credentials/discordToken.json").getLines().mkString
  val parseResult: Either[ParsingFailure, Json] = parse(fileContent)
  val removeCharacters: Set[Char] = "\"".toSet
  val token: String = parseResult.right.get.\\("token")(0).toString().filterNot(removeCharacters)

  val clientSettings: ClientSettings = ClientSettings(token)
  val client: DiscordClient = Await.result(clientSettings.createClient(), Duration.Inf)

  implicit val timeout: Timeout = Timeout(3 seconds)

  var actorOutput: String = ""

  client.onEventSideEffects { implicit c => {
    case APIMessage.MessageCreate(_, message, _) => {
      if (message.content.startsWith("!")) {

        if (message.content.equals("!Hello")) {
          self ! message.content + message.authorUsername
        }
        else if (message.content.equals("!Start")) {

          var listBuffer: ListBuffer[(String, String, String)] = new ListBuffer[(String, String, String)]()
          var messageId: Option[data.MessageId] = Some(message.id)
          var iterations: Int = 0
          do{
            messageId match {
              case Some(id: data.MessageId) => {
                val test: Option[Seq[RawMessage]] = Await.result(client.requestsHelper.run(GetChannelMessages(message.channelId, GetChannelMessagesData(before = Some(id), limit = Some(100)))).value, 1 minute)
                test match {
                  case Some(value: List[RawMessage]) => {
                    value.map(cont => {
                      listBuffer. += ((cont.timestamp.toString, cont.author.username,cont.content))
                      messageId = Some(cont.id)
                    })
                  }
                  case None => messageId = None
                }
              }
              case None => messageId = None
            }
            iterations = iterations + 1
          }while(iterations < 20)

          val messageList: List[(String, String, String)] = listBuffer.toList

          val stringList: List[String] = messageList.map(content => content._1 + "%" + content._2 + "%" + content._3.replaceAll("\r\n", "{NL}") + "\n")
          val reversedStringList: List[String] = stringList.reverse
          writeFile("Source.txt", reversedStringList)
          println("Done")
        }
        else {
          self ! message.content
        }
        Thread.sleep(500)
        client.requestsHelper.run(CreateMessage(message.channelId, CreateMessageData(content = actorOutput))
          .map(_ => ()))
        if (message.content.equals("!Goodbye")) {
          clientSettings.system.terminate()
        }
      }
    }
  }
  }
  client.login()

  override def receive: Receive = {
    case msg: String => {
      val future: Future[Any] = chatServer ? msg.substring(1)
      val result: Any = Await.result(future, timeout.duration)
      actorOutput = result.toString
    }
    case _ =>
  }
  def writeFile(filename: String, lines: List[String]): Unit = {
    val file: File = new File(filename)
    val bw: BufferedWriter = new BufferedWriter(new FileWriter(file))
    for(line <- lines){
      bw.write(line)
    }
    bw.close()
  }
}