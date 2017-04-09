package model

import play.api.libs.json.Json

case class CurrentLocation(car: Car, cabby: Cabby, position: Position) {

}

object CurrentLocation {
  implicit val jsonFormat = Json.format[CurrentLocation]
}
