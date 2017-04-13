package services

import model.{Cabby, Passenger, Person, Position}

import scala.collection.mutable.{HashMap, Set => MutableSet}
import scala.util.Try

trait Maps[T <: Person] {

  val roads: Array[Array[Boolean]]
  val people = new HashMap[Position, MutableSet[T]]

  def unblocked(position: Position): Boolean = {
    Try(roads(position.x)(position.y)).getOrElse(false)
  }

  def find(person: T): Option[T] = {
    people.get(person.currentPosition).flatMap(_.find(otherPerson => {
      otherPerson.equals(person) && otherPerson.currentPosition.equals(person.currentPosition)
    }))
  }

  def list(position: Position): Set[T] = {
    people.get(position).filter(_.nonEmpty).fold(Set.empty[T])(x => x.toSet)
  }

  def updatePosition(person: T, newPosition: Position): Unit = {
    val peoplePosition = people(person.currentPosition)
    if(peoplePosition.contains(person)) peoplePosition.remove(person)
    this.add(updatedPersonPosition(person, newPosition))
  }

  protected def updatedPersonPosition(person: T, newPosition: Position): T

  def add(person: T): Unit = people.get(person.currentPosition)
    .filter(_.nonEmpty)
    .fold(ifEmpty = if(unblocked(person.currentPosition)) {
      this.people.put(person.currentPosition, MutableSet(person))
    })(cabbies => {
      if(cabbies.contains(person)) cabbies -= person
      cabbies += person
    })

}

case class CabbiesRoadMap(roads: Array[Array[Boolean]]) extends CabbiesMap
trait CabbiesMap extends Maps[Cabby] {

  override protected def updatedPersonPosition(cabby: Cabby, newPosition: Position): Cabby = {
   cabby.copy(currentPosition = newPosition)
  }
}

case class PassengersRoadMap(roads: Array[Array[Boolean]]) extends PassengersMap
trait PassengersMap extends Maps[Passenger] {

  override protected def updatedPersonPosition(cabby: Passenger, newPosition: Position): Passenger = {
    cabby.copy(currentPosition = newPosition)
  }

}
