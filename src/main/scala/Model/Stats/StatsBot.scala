package Model.Stats

import akka.actor.Actor

class StatsBot extends Actor{
  val stats: String =
    """
      |Hello here come the Stats for your server:
      |Messages from: (Name) => x Messages written
      |""".stripMargin

  override def receive: Receive = {
    case "Stats" => sender() ! stats
    case _ =>
  }
}

