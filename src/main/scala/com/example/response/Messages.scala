package com.example.response


object Messages {

  case class SuccessfulOperation(key: String)

  case class Result(key: String, value: Option[Object])

}
