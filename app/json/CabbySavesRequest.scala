package json

import model.CurrentLocation
import play.api.libs.json.Json

case class CabbySavesRequest(currentLocation: CurrentLocation)

object CabbySavesRequest {
  implicit val jsonFormat = Json.format[CabbySavesRequest]
}
