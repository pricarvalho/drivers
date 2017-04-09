package json

package json

import model.{Passenger, Position, Route}
import play.api.libs.json.Json

import scala.util.Random

case class PassengerSavesRequest(originPosition: Position, targetPosition: Position) {

  def toPassenger: Passenger = Passenger(id = new Random().nextLong(), route)

  private def route = Route(originPosition, targetPosition)

}

object PassengerSavesRequest {
  implicit val jsonFormat = Json.format[PassengerSavesRequest]
}
