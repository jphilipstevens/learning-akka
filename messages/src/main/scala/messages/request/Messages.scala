package messages.request


object Messages {

  case class StoreObject(key: String, value: Object)

  case class GetObject(key: String)

  case class SetIfNotExist(key: String, value: Object)

}
