package model

import play.api.libs.json.Json

case class Car(tag: String) {

}

object Car {
  implicit val jsonFormat = Json.format[Car]
}
