package com.example


import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import messages.request.Messages.{GetObject, StoreObject}
import messages.response.Messages.{Result, SuccessfulOperation, UnknownMessage}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FunSpecLike, Matchers}

class AkkaDbSpec extends TestKit(ActorSystem("test-system"))
  with ImplicitSender with FunSpecLike with Matchers with BeforeAndAfterAll with BeforeAndAfterEach {

  val actorRef = TestActorRef[AkkaDb]
  val akkaDb = actorRef.underlyingActor

  object TestConfiguration {
    val Timeout = 1 seconds
  }

  override def beforeEach(): Unit = {
    akkaDb.map.clear()
  }

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  describe("the Storage of a Key") {
    it("should store the key in the database if it does not exist") {
      val key = "key"
      val valueToStore = "storeMe"
      akkaDb.map.get(key) should be(None)
      actorRef ! StoreObject(key, valueToStore)
      expectMsg(TestConfiguration.Timeout, SuccessfulOperation(key))
    }

    it("should override the previous value in the database") {
      val key = "key"
      val valueToStore = "storeMeAgain"
      akkaDb.map.put(key, valueToStore)
      akkaDb.map.get(key) should not be None
      actorRef ! StoreObject(key, valueToStore)
      akkaDb.map.get(key) should be (Some(valueToStore))
      expectMsg(TestConfiguration.Timeout, SuccessfulOperation(key))
    }
  }

  describe("The Fetching of a key") {
    it("return nothing when the store is empty") {
      val key = "key"
      val valueToStore = "storeMe"
      akkaDb.map.put(key, valueToStore)
      actorRef ! GetObject(key)
      expectMsg(TestConfiguration.Timeout, Result(key, Some(valueToStore)))
    }

    it("return the value stored in the DB") {
      val key = "key"
      actorRef ! GetObject("key")
      expectMsg(TestConfiguration.Timeout, Result("key", None))
    }
  }

  describe("Sending am unknown request") {
    it("Return an Unknown message ") {
      actorRef ! "I should not exist"
      expectMsg(TestConfiguration.Timeout, UnknownMessage)
    }
  }
}
