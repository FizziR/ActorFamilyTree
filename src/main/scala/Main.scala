import akka.NotUsed
import akka.actor.{ActorSystem, Props}
import akka.stream.ClosedShape
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source, Zip}

import scala.collection.mutable.ListBuffer

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

    val pasteSpaceBetweenArguments = builder.add(Flow[String].map(string => string.replace('%', ' ')))
    val messageParserModel = new MessageParserModel()
    val convertsStringToMessage = builder.add(Flow[String].map(message => messageParserModel.generateMessageFromString(message)))

    val output = builder.add(Sink.foreach[Option[Message]](println))


    //val broadcast = builder.add(Broadcast[String](2))
    //val zip = builder.add(Zip[(String, String, String), String])

    // step 3 - tying up the components

    input ~> pasteSpaceBetweenArguments ~> convertsStringToMessage ~> output

    /*input ~> broadcast
    broadcast.out(0) ~> splitStringsInTuples ~> zip.in0
    zip.out ~> output*/

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
