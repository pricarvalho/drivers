package json

import model.{Cabby, Position, Unappropriated}
import play.api.libs.json.Json

case class CabbySavesRequest(tagCar: String, currentPosition: Position) {

  def toUnappropriated: Unappropriated = Unappropriated(Cabby(tagCar), currentPosition)

}

object CabbySavesRequest {
  implicit val jsonFormat = Json.format[CabbySavesRequest]
}
