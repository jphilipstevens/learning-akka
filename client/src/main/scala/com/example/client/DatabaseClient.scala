package com.example.client

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import messages.request.Messages.{GetObject, StoreObject}

import scala.concurrent.Future
import scala.concurrent.duration._

class DatabaseClient(
  val databaseAddress : String
) {
  val databasePath = s"akka.tcp://akka-db@$databaseAddress/user/akka-db"

  private implicit val timeout = Timeout(2 seconds)
  private implicit val system = ActorSystem("LocalSystem")
  private val database = system.actorSelection(databasePath)

  def save(key: String, value: Object): Future[Any] = database ? StoreObject(key, value)

  def get(key: String): Future[Any] =  database ? GetObject(key)
}
