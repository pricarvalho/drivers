package json

package json

import model.{Passenger, Position, Route}
import play.api.libs.json.Json

case class PassengerSavesRequest(originPosition: Position, targetPosition: Position) {

  def toPassenger: Passenger = Passenger(route)

  private def route = Route(originPosition, targetPosition)

}

object PassengerSavesRequest {
  implicit val jsonFormat = Json.format[PassengerSavesRequest]
}
