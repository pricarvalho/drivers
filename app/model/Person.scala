package model

import play.api.libs.json.Json

trait Person{
  val currentPosition: Position
}

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

  implicit def update(cabby: Cabby, newPosition: Position): Cabby = {
    cabby.copy(currentPosition = newPosition)
  }
}

case class Passenger(id: Long, currentPosition: Position, route: Route) extends Person {

  override def equals(obj: scala.Any): Boolean = obj match {
    case other: Passenger => this.id.equals(other.id)
    case _ => false
  }

  override def hashCode: Int = id.hashCode + currentPosition.hashCode
}
object Passenger {

  implicit def update(passenger: Passenger, newPosition: Position): Passenger = {
    passenger.copy(currentPosition = newPosition)
  }

  def apply(id: Long, route: Route): Passenger = new Passenger(id, route.originPosition, route)

  implicit val jsonFormat = Json.format[Cabby]

}
