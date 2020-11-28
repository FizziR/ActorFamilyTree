import Controller.Controller
import _root_.Controller.Discord.DiscordClient

object Main{
  val discordClient = new DiscordClient
  discordClient.login()
  val controller = new Controller(discordClient)

  def main(args: Array[String]) = {
    do{

    }while(discordClient.discordInput != "!Goodbye")

    discordClient.logout()
  }
}
