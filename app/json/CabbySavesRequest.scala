package json

import model.{Cabby, Position}
import play.api.libs.json.Json

case class CabbySavesRequest(tagCar: String, currentPosition: Position) {

  def toCabby: Cabby = Cabby(tagCar, currentPosition)

}

object CabbySavesRequest {
  implicit val jsonFormat = Json.format[CabbySavesRequest]
}
