package model

import play.api.libs.json.Json

case class Passenger(id: Long, route: Route) {

}

object Passenger {
  implicit val jsonFormat = Json.format[Passenger]
}
