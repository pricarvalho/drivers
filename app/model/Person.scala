package model

import play.api.libs.json.Json

trait Person {

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
  implicit val clazz = classOf[Cabby]
}

case class Passenger(id: Long, currentPosition: Position, route: Route) extends Person {


}

object Passenger {

  def apply(id: Long, route: Route): Passenger = Passenger(id, route.originPosition, route)

  implicit val jsonFormat = Json.format[Cabby]
  implicit val clazz = classOf[Passenger]

}
