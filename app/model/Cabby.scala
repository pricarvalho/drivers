package model

import play.api.libs.json.Json

case class Cabby(tagCar: String) {

}

object Cabby {
  implicit val jsonFormat = Json.format[Cabby]
}
