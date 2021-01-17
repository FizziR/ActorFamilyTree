package Controller

import java.io.{BufferedWriter, File, FileWriter}
import ackcord.{APIMessage, ClientSettings}
import ackcord.requests.{CreateMessage, CreateMessageData}
import akka.actor.{Actor, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, DurationInt}
import io.circe.parser._

class DiscordBot extends Actor {
  val chatServer = context.actorOf(Props[ChatServer], name = "chatServer")

  val fileContent = scala.io.Source.fromFile("Credentials/discordToken.json").getLines().mkString
  val parseResult = parse(fileContent)
  val removeCharacters = "\"".toSet
  val token = parseResult.right.get.\\("token")(0).toString().filterNot(removeCharacters)

  val clientSettings = ClientSettings(token)
  val client = Await.result(clientSettings.createClient(), Duration.Inf)

  implicit val timeout: Timeout = Timeout(3 seconds)

  var actorOutput = ""

  client.onEventSideEffects { implicit c => {
    case APIMessage.MessageCreate(_, message, _) => {

      val messageMetaString = message.timestamp + "%" + message.authorUsername + "%" + message.content.replaceAll("\r\n", "{NL}") + "\n"
      addMessageToSourceFile("Source.txt", messageMetaString)

      if (message.content.startsWith("!")) {
        if (message.content.equals("!Hello")) {
          self ! message.content + message.authorUsername
        }
        /*else if (message.content.equals("!Start")) {

          var listBuffer = new ListBuffer[(String, String, String)]()
          var messageId: Option[data.MessageId] = Some(message.id)
          var iterations = 0
          do{
            messageId match {
              case Some(id: data.MessageId) => {
                val test = Await.result(client.requestsHelper.run(GetChannelMessages(message.channelId, GetChannelMessagesData(before = Some(id), limit = Some(100)))).value, 1 minute)
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

          val messageList = listBuffer.toList

          val stringList = messageList.map(content => content._1 + "%/%" + content._2 + "%/%" + content._3.replaceAll("\r\n", "{NL}") + "\n")
          val reversedStringList = stringList.reverse
          writeFile("Source.txt", reversedStringList)
          println("Done")
        }*/
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
      val future = chatServer ? msg.substring(1)
      val result = Await.result(future, timeout.duration)
      actorOutput = result.toString
    }
    case _ =>
  }
  def writeFile(filename: String, lines: List[String]): Unit = {
    val file = new File(filename)
    val bw = new BufferedWriter(new FileWriter(file))
    for(line <- lines){
      bw.write(line)
    }
    bw.close()
  }
  def addMessageToSourceFile(filename: String, message: String): Unit ={
    val fw = new FileWriter(filename, true)
    fw.write(message)
    fw.close()
  }
}