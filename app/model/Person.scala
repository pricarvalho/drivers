package model

import play.api.libs.json.Json

trait Person{
  val currentPosition: Position
}

case class Cabby(tagCar: String, currentPosition: Position, val status: Int) extends Person {

  def empty = status == 1
  def onTheWay = status == 2
  def full = status == 3

  override def equals(obj: scala.Any): Boolean = obj match {
    case other: Cabby => this.tagCar.equals(other.tagCar)
    case other: String => this.tagCar.equals(other)
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
