import Kafka.ProducerContent
import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.{ClosedShape, FanInShape2, FlowShape, Graph, SinkShape, SourceShape, UniformFanInShape, UniformFanOutShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source, Zip}

import scala.collection.mutable.Queue
import scala.collection.mutable.ListBuffer
import Controller.DiscordBot

object Main{

  // Source
  implicit val sourceSystem: ActorSystem = ActorSystem("sourceSystem")
  val discordBot: ActorRef = sourceSystem.actorOf(Props[DiscordBot], name = "discordBot")

  import Kafka.Producer
  val producer: Producer = new Producer
  var sinkQueue: Queue[ProducerContent] = new Queue[ProducerContent]

  // step 1 - setting up the fundamentals for the graph
  val graph: Graph[ClosedShape.type, NotUsed] = GraphDSL.create(){ implicit builder:
    GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._

  // step 2 - add the necessary components of this graph
    val input: SourceShape[String] = builder.add(Source(readSource()))

    import Model.MessageParser.MessageParserModel
    import Model.Message

    val pasteSpaceBetweenArguments: FlowShape[String, String] = builder.add(Flow[String].map(string => string.replace('%', ' ')))
    val messageParserModel: MessageParserModel = new MessageParserModel()
    val convertsStringToMessage: FlowShape[String, Option[Message]] = builder.add(Flow[String].map(message => messageParserModel.generateMessageFromString(message)))

    val convertOptionToMessage: FlowShape[Option[Message], Message] = builder.add(Flow[Option[Message]].map(message => message.getOrElse(Message(null, "", ""))))

    val convertMessageToUser: FlowShape[Message, String] = builder.add(Flow[Message].map(message => message.user))
    val convertMessageToWord: FlowShape[Message, Int] = builder.add(Flow[Message].map(message => message.message.split(" ").length))
    val convertMessageToChar: FlowShape[Message, Int] = builder.add(Flow[Message].map(message => message.message.length))

  val convertTuplesToProducerContent: FlowShape[(String, (Int, Int)), ProducerContent] = builder.add(Flow[(String, (Int, Int))].map(message => ProducerContent(message._1, message._2._1, message._2._2)))

    val output: SinkShape[ProducerContent] = builder.add(Sink.foreach[ProducerContent](sinkQueue.enqueue(_)))

    val broadcast: UniformFanOutShape[Message, Message] = builder.add(Broadcast[Message](3))

    val wordZip: FanInShape2[Int, Int, (Int, Int)] = builder.add(Zip[Int, Int])

    val zip: FanInShape2[String, (Int, Int), (String, (Int, Int))] = builder.add(Zip[String, (Int, Int)])

    // step 3 - tying up the components*/

    input ~> pasteSpaceBetweenArguments ~> convertsStringToMessage ~> convertOptionToMessage ~> broadcast
    broadcast.out(0) ~> convertMessageToUser ~> zip.in0
    broadcast.out(1) ~> convertMessageToWord ~> wordZip.in0
    broadcast.out(2) ~> convertMessageToChar ~> wordZip.in1
                                 wordZip.out ~> zip.in1
    zip.out ~> convertTuplesToProducerContent ~> output
    ClosedShape
  }

  def main(args: Array[String]): Unit = {
    RunnableGraph.fromGraph(graph).run()
    while(true){
      if( !sinkQueue.isEmpty){
        val message: ProducerContent = sinkQueue.dequeue()
        producer.produceInput(message)
        println("Queue length: " + sinkQueue.length + " Message: " + message)
      }
      else{
        println("Queue is empty")
      }
      Thread.sleep(500)
    }
  }

  def readSource(): List[String] ={
    var lineBuffer: ListBuffer[String] = new ListBuffer[String]()
    for(line <- scala.io.Source.fromFile("Resources//Source.txt").getLines()){
      lineBuffer += line
    }
    lineBuffer.toList
  }
}
