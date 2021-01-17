import akka.NotUsed
import akka.actor.{ActorSystem, Props}
import akka.stream.ClosedShape
import akka.stream.scaladsl.{Flow, GraphDSL, RunnableGraph, Sink, Source}
import scala.collection.mutable.ListBuffer
import Controller.DiscordBot

object Main{

  // Source
  implicit val sourceSystem = ActorSystem("sourceSystem")
  val discordBot = sourceSystem.actorOf(Props[DiscordBot], name = "discordBot")

  // step 1 - setting up the fundamentals for the graph
  val graph = GraphDSL.create(){ implicit builder:
    GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._

    // step 2 - add the necessary components of this graph
    val input = builder.add(Source(readSource()))

    val splitStringsInTuples = builder.add(Flow[String].map(i => {
      i.split("\\%+") match {
        case Array(timestamp: String, author: String, message: String) => (timestamp, author, message)
        case _ => ("", "", "")
      }
    }))
    val output = builder.add(Sink.foreach[(String, String, String)](println))

    // step 3 - tying up the components
    input ~> splitStringsInTuples ~> output

    ClosedShape
  }

  def main(args: Array[String]): Unit = {
    RunnableGraph.fromGraph(graph).run()
  }

  def readSource(): List[String] ={
    var lineBuffer = new ListBuffer[String]()
    for(line <- scala.io.Source.fromFile("Source.txt").getLines()){
      lineBuffer += line
    }
    lineBuffer.toList
  }
}
