package json

import java.util.UUID

import play.api.libs.json.Json

case class CabbyRequest(passenger: String) {

  def uuidPassenger = UUID.fromString(passenger)

}

object CabbyRequest {
  implicit val jsonFormat = Json.format[CabbyRequest]
}
