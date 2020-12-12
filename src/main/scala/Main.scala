import akka.NotUsed
import akka.actor.{ActorSystem, Props}
import akka.stream.{ClosedShape, FlowShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source, Zip}
import shapeless.syntax.std.tuple.productTupleOps

import scala.::
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

    val pasteSpaceBetweenArguments= builder.add(Flow[String].map(string => string.replace('%', ' ')))
    val messageParserModel = new MessageParserModel()
    val convertsStringToMessage = builder.add(Flow[String].map(message => messageParserModel.generateMessageFromString(message)))

    val convertOptionToMessage = builder.add(Flow[Option[Message]].map(message => message.getOrElse(Message(null, "", ""))))

    val convertMessageToUser = builder.add(Flow[Message].map(message => message.user))
    val convertMessageToWord = builder.add(Flow[Message].map(message => message.message.split(" ").length))
    val convertMessageToChar = builder.add(Flow[Message].map(message => message.message.length))


    val output = builder.add(Sink.foreach[(String, (Int, Int))](println))


    val broadcast = builder.add(Broadcast[Message](2))

    val wordBroadcast = builder.add(Broadcast[Message](2))
    val wordZip = builder.add(Zip[Int, Int])

    val zip = builder.add(Zip[String, (Int, Int)])

    // step 3 - tying up the components*/

    input ~> pasteSpaceBetweenArguments ~> convertsStringToMessage ~> convertOptionToMessage ~> broadcast

    broadcast.out(0) ~> convertMessageToUser ~> zip.in0
    broadcast.out(1) ~> wordBroadcast
                        wordBroadcast.out(0) ~> convertMessageToWord ~> wordZip.in0
                        wordBroadcast.out(1) ~> convertMessageToChar ~> wordZip.in1

                        wordZip.out ~> zip.in1

    zip.out ~> output

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
