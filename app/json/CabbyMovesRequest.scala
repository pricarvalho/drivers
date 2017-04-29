package json

import play.api.libs.json.Json

case class CabbyMovesRequest(passenger: String, time: Int = 1) {

}

object CabbyMovesRequest {
  implicit val jsonFormat = Json.format[CabbyMovesRequest]
}
