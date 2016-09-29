package com.example

import akka.actor.{Actor, ActorRef}
import messages.request.Messages._
import messages.response.Messages._

import scala.collection.mutable

class AkkaDb extends Actor {

  val map = mutable.Map.empty[String, Object]

  def storeObject(key: String, value: Object)(sender: ActorRef): Unit = {
    map.put(key, value)
    sender ! SuccessfulOperation(key)
  }

  def setKey(key: String, value: Object)(sender: ActorRef): Unit = {
    map.getOrElseUpdate(key, () => value)
    sender ! SuccessfulOperation(key)
  }

  def delete(key: String)(sender: ActorRef): Unit = {
    map.remove(key)
    sender ! SuccessfulOperation(key)
  }

  override def receive: Receive = {
    case StoreObject(key, value) => storeObject(key, value)(sender())
    case GetObject(key) => sender() ! Result(key, map.get(key))
    case SetIfNotExist(key, value) => setKey(key, value)(sender())
    case _ => sender() ! AkkaDb.UnknownMessage

  }
}

object AkkaDb {
  val UnknownMessage = "unknown message"

}