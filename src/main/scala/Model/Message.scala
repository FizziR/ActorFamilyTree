package Model

import akka.http.scaladsl.model.DateTime

case class Message(dateTime: DateTime, user:String, message: String){}
