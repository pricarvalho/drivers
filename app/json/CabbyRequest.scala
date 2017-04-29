package json

import model.{Cabby, Passenger}
import play.api.libs.json.Json

case class CabbyRequest(tagCar: String, idPassenger: Long) {

  def findCabby(find: String => Option[Cabby]): Option[Cabby] = find(tagCar)

  def findPassenger(find: Long => Option[Passenger]): Option[Passenger] = find(idPassenger)

}

object CabbyRequest {
  implicit val jsonFormat = Json.format[CabbyRequest]
}
