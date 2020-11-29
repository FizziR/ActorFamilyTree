package Util

trait Observer {
  def update: Unit
}

class Observable {
  var subscribers: Vector[Observer] = Vector()

  def add(s: Observer): Unit = subscribers = subscribers :+ s

  def notifyObservers: Unit = subscribers.foreach(o => o.update)
}
