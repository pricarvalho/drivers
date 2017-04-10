package model

import play.api.libs.json.Json

trait Person

case class Cabby(tagCar: String, currentPosition: Position, private val statusCode: Int) extends Person {

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

case class Passenger(id: Long, route: Route) extends Person {

  def currentPosition: Position = route.originPosition

}

object Passenger {
  implicit val jsonFormat = Json.format[Cabby]
}
