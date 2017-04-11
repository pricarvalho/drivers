package services

import model.{Cabby, Passenger, Person, Position}

import scala.collection.mutable.{HashMap, ListBuffer}
import scala.util.Try

trait Maps {

  def unblocked(position: Position): Boolean
  def listIn[T](position: Position)(implicit clazz: Class[T]): ListBuffer[T]
  def add(person: Person): Unit
  def update(person: Person, newPosition: Position): Unit
}

case class RoadMap(roads: Array[Array[Boolean]]) extends Maps {

  type PeopleByPosition = HashMap[Position, ListBuffer[Person]]

  private val people: PeopleByPosition = new PeopleByPosition

  override def unblocked(position: Position): Boolean = {
    Try(roads(position.x)(position.y)).getOrElse(false)
  }

  override def listIn[T](position: Position)(implicit clazz: Class[T]): ListBuffer[T] = {
    people.get(position).filter(_.nonEmpty).fold(ListBuffer.empty[T])(x => {
      x.filter(clazz.isInstance(_)).asInstanceOf[ListBuffer[T]]
    })
  }

  override def add(person: Person): Unit = people.get(person.currentPosition)
    .filter(_.nonEmpty)
    .fold(ifEmpty = if(unblocked(person.currentPosition)) {
      this.people.put(person.currentPosition, ListBuffer(person))
    })(cabbies => {
      if(cabbies.contains(person)) cabbies -= person
      cabbies += person
    })

  override def update(person: Person, newPosition: Position): Unit = {
    val filtered = people(person.currentPosition).filterNot(_.equals(person))
    this.people.updated(person.currentPosition, filtered)
    val newPersonPosotion = person match {
      case cabby: Cabby => cabby.copy(currentPosition = newPosition)
      case passenger: Passenger => passenger.copy(currentPosition = newPosition)
    }
    this.add(newPersonPosotion)
  }

}
