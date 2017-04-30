package json

import play.api.libs.json.Json

case class DriverMovesRequest(passenger: String, time: Int = 1) {

}

object DriverMovesRequest {
  implicit val jsonFormat = Json.format[DriverMovesRequest]
}
