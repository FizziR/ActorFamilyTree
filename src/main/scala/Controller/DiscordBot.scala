import ackcord.{APIMessage, CacheSnapshot, ClientSettings, data}
import ackcord.requests.{CreateMessage, CreateMessageData}
import akka.actor.{Actor, Props}
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, DurationInt}
import io.circe.parser._

import scala.collection.mutable.{ArrayBuffer, Queue}
import akka.stream.scaladsl.{Flow, GraphDSL, Sink, Source}

class DiscordBot extends Actor {
  val log = Logging(context.system, this)
  val chatServer = context.actorOf(Props[ChatServer], name = "chatServer")

  val fileContent = scala.io.Source.fromFile("Credentials/discordToken.json").getLines().mkString

  val parseResult = parse(fileContent)

  val removeCharacters = "\"".toSet
  val token = parseResult.right.get.\\("token")(0).toString().filterNot(removeCharacters)

  val clientSettings = ClientSettings(token)

  val client = Await.result(clientSettings.createClient(), Duration.Inf)

  implicit val timeout: Timeout = Timeout(3 seconds)

  var actorOutput = ""
  var bufferQueue = Queue[String]()

  val thread = new Thread {
    override def run: Unit = {
      while (true) {
        if (bufferQueue.length != 0) {
          println("Queue: " + bufferQueue)
          Main.runGraph(bufferQueue.dequeueAll(_.length > 0).toList)
        }
        Thread.sleep(5000)
      }
    }
  }.start()

  client.onEventSideEffects { implicit c => {
    case APIMessage.MessageCreate(_, message, _) => {
      if (message.content.startsWith("!")) {

        //  Stream implementation
        bufferQueue.enqueue(message.content)

        if (message.content.equals("!Hello")) {
          self ! message.content + message.authorUsername
        }
        else if (message.content.equals("!Start")) {
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
        if (message.content.equals("!Goodbye")) {
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
    case msg: String => {
      val future = chatServer ? msg.substring(1)
      val result = Await.result(future, timeout.duration)
      log.info(result.toString)
      actorOutput = result.toString
    }
    case _ =>
  }

  /*def createGraph(buffer: Seq[(String, data.TextChannelId)]): Graph[Unit, NotUsed] = {
    val graph = GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] => {
      //  Source
      val input = builder.add(Source(buffer.toList))

      // FLows
      val messageHandler = builder.add(Flow[(String, data.TextChannelId)].map(x => self ! x._1))

      // Sink
      val output = builder.add(Sink.foreach[(String, data.TextChannelId)](println))

      }
    }
    graph
  }*/
}