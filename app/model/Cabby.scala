package model

import play.api.libs.json.Json

case class Cabby(id: Long) {

}

object Cabby {
  implicit val jsonFormat = Json.format[Cabby]
}
