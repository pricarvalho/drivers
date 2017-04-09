package model

import play.api.libs.json.Json

case class Cabby(tagCar: String, currentPosition: Position) {

  override def equals(obj: scala.Any): Boolean = obj match {
    case other: Cabby => this.tagCar.equals(other.tagCar)
    case _ => false
  }

  override def hashCode: Int = tagCar.hashCode + currentPosition.hashCode

}

object Cabby {
  implicit val jsonFormat = Json.format[Cabby]
}
