package model

import play.api.libs.json.Json

case class Cabby(tagCar: String, currentPosition: Position, private val statusCode: Int) {

  def empty = statusCode == 1
  def onTheWay = statusCode == 2
  def full = statusCode == 3

  override def equals(obj: scala.Any): Boolean = obj match {
    case other: Cabby => this.tagCar.equals(other.tagCar)
    case _ => false
  }

  override def hashCode: Int = tagCar.hashCode + currentPosition.hashCode

}

object Cabby {
  implicit val jsonFormat = Json.format[Cabby]
}
