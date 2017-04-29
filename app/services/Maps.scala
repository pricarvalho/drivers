package services

import java.util.UUID

import model.{Cabby, Passenger, Person, Position}

import scala.collection.mutable.{HashMap, Set => MutableSet}
import scala.util.Try

trait Maps[T <: Person] {

  val roads: Array[Array[Boolean]]
  val people = new HashMap[Position, MutableSet[T]]

  def find(obj: Any): Option[T] = people.values.flatten[T].find(_.equals(obj))

  def unblocked(position: Position): Boolean = {
    Try(roads(position.x)(position.y)).getOrElse(false)
  }

  def list(position: Position): Set[T] = {
    people.get(position).filter(_.nonEmpty).fold(Set.empty[T])(x => x.toSet)
  }

  def movePosition(person: T, newPosition: Position)(implicit f: (T, Position) => T): Unit = {
    val peoplePosition = people(person.currentPosition)
    if(peoplePosition.contains(person)) peoplePosition.remove(person)
    this.add(f(person,newPosition))
  }

  def add(person: T): UUID = {
    people.get(person.currentPosition)
      .filter(_.nonEmpty)
      .fold(ifEmpty = if(unblocked(person.currentPosition)) {
        this.people.put(person.currentPosition, MutableSet(person))
      })(people => {
        people.remove(person)
        people += person
      })
    person.uuid
  }
}

case class CabbiesRoadMap(roads: Array[Array[Boolean]]) extends CabbiesMap
trait CabbiesMap extends Maps[Cabby]

case class PassengersRoadMap(roads: Array[Array[Boolean]]) extends PassengersMap
trait PassengersMap extends Maps[Passenger]
