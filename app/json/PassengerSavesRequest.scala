package json

package json

import model.{Passenger, Route}
import play.api.libs.json.Json

import scala.util.Random

case class PassengerSavesRequest(route: Route) {

  def toPassenger: Passenger = Passenger(id = new Random().nextLong(), route = route)

}

object PassengerSavesRequest {
  implicit val jsonFormat = Json.format[PassengerSavesRequest]
}
