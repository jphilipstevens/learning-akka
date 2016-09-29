package com.example

import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system = ActorSystem("akka-database")
  val database = system.actorOf(Props[AkkaDb], name = "akka-database")
}
