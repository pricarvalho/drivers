package model

import java.util.UUID

import play.api.libs.json._

trait Person {
  val uuid: UUID
  val currentPosition: Position
}

object PersonWrites extends OWrites[Person] {
  def writes(person: Person) = Json.obj("uuid" -> person.uuid.toString)
}

case class Cabby(tagCar: String, currentPosition: Position, status: Int, uuid: UUID = UUID.randomUUID) extends Person {

  def empty = status == 1
  def onTheWay = status == 2
  def full = status == 3

  override def equals(obj: Any): Boolean = obj match {
    case other: Cabby => this.tagCar.equals(other.tagCar) && this.uuid.equals(other.uuid)
    case other: String => this.tagCar.equals(other)
    case other: UUID => this.uuid.equals(other)
    case _ => false
  }

  override def hashCode: Int = tagCar.hashCode + currentPosition.hashCode

}

object Cabby {

  implicit val jsonFormat = new OFormat[Cabby] {
    override def reads(json: JsValue): JsResult[Cabby] = Json.format[Cabby].reads(json)
    override def writes(o: Cabby): JsObject = Json.format[Cabby].writes(o)++PersonWrites.writes(o)
  }

  implicit def update(cabby: Cabby, newPosition: Position): Cabby = {
    cabby.copy(currentPosition = newPosition)
  }
}

case class Passenger(currentPosition: Position, route: Route, uuid: UUID = UUID.randomUUID) extends Person {

  lazy val targetPosition: Position = this.route.targetPosition

  override def equals(obj: Any): Boolean = obj match {
    case other: Passenger => this.uuid.equals(other.uuid)
    case other: UUID => this.uuid.equals(other)
    case _ => false
  }

  override def hashCode: Int = uuid.hashCode + currentPosition.hashCode
}

object Passenger {

  implicit val jsonFormat = new OFormat[Passenger] {
    override def reads(json: JsValue): JsResult[Passenger] = Json.format[Passenger].reads(json)
    override def writes(o: Passenger): JsObject = Json.format[Passenger].writes(o)++PersonWrites.writes(o)
  }

  implicit def update(passenger: Passenger, newPosition: Position): Passenger = {
    passenger.copy(currentPosition = newPosition, uuid = passenger.uuid)
  }

  def apply(route: Route): Passenger = new Passenger(route.originPosition, route)

}
