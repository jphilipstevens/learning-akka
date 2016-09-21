package com.example

import akka.actor.{Actor, ActorRef}

import scala.collection.mutable

case class StoreObject(key: String, value: Object)
case class GetObject(key: String)

case class SuccessfulOperation(key: String)
case class Result(key: String, value: Option[Object])


class AkkaDb extends Actor {

  val map = mutable.Map.empty[String, Object]

  def storeObject(key: String, value: Object)(sender: ActorRef) : Unit = {
    map.put(key, value)
    sender ! SuccessfulOperation(key)
  }

  override def receive: Receive = {
    case StoreObject(key, value) => storeObject(key, value)(sender())
    case GetObject(key) => sender() ! Result(key, map.get(key))
    case _ => sender() ! AkkaDb.UnknownMessage
  }
}

object AkkaDb {
  val UnknownMessage = "unknown message"

}