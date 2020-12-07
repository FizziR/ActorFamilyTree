import ackcord.data
import akka.NotUsed
import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.stream.ClosedShape
import akka.stream.javadsl.GraphDSL
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

object Main{

  // Source
  implicit val sourceSystem = ActorSystem("sourceSystem")
  val discordBot = sourceSystem.actorOf(Props[DiscordBot], name = "discordBot")

  implicit val timeout: Timeout = Timeout(2 seconds)
  val flow = Flow[String].map(x => {
    val result = Await.result(discordBot ? x, timeout.duration)
    result.toString
  })
  val sink = Sink.foreach[String](x => println("Result: " + x))

  // Flow

  /*val messageSystem = ActorSystem("messageSystem")
  val messageBot = messageSystem.actorOf(Props[MessageBot], name = "calculationBot")

  val lectureSystem = ActorSystem("calculationSystem")
  val lectureBot = lectureSystem.actorOf(Props[LectureBot], name = "lectureBot")*/

  // Sink

  def runGraph(list: List[String]): Unit ={
    println("List: " + list)
    val source = Source(list)
    val graph = source.via(flow).to(sink)
    graph.run()
  }

  def main(args: Array[String]) = {

  }
}
