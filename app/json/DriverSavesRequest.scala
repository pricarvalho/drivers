package json

import model.{Driver, Position}
import play.api.libs.json.Json

case class DriverSavesRequest(tagCar: String, currentPosition: Position) {

  def toDriver: Driver = Driver(tagCar, currentPosition, status = 1)

}

object DriverSavesRequest {
  implicit val jsonFormat = Json.format[DriverSavesRequest]
}
