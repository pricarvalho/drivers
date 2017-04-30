package services

import model.{Driver, Passenger, Person, Position}

import scala.collection.mutable.{HashMap, Set => MutableSet}
import scala.util.Try

trait Maps[T <: Person] {

  val roads: Array[Array[Boolean]]
  val people = new HashMap[Position, MutableSet[T]]

  def unblocked(position: Position): Boolean = {
    Try(roads(position.x)(position.y)).getOrElse(false)
  }

  def find(obj: Any): Option[T] = people.values.flatten[T].find(_.equals(obj))

  def list(position: Position): Set[T] = {
    people.get(position).filter(_.nonEmpty).fold(Set.empty[T])(x => x.toSet)
  }

  def update(person: T) = remove(person).add(person)

  def movePosition(person: T, newPosition: Position)(implicit f: (T, Position) => T): Option[T] = {
    remove(person).add(f(person,newPosition))
  }

  def add(person: T): Option[T] = {
    people.get(person.currentPosition)
      .filter(_.nonEmpty)
      .fold(ifEmpty = if(unblocked(person.currentPosition)) {
        this.people.put(person.currentPosition, MutableSet(person))
      })(people => {
        people.remove(person)
        people += person
      })
    this.find(person.uuid)
  }

  private def remove(person: T) = {
    val peoplePosition = Try(people(person.currentPosition))
    peoplePosition.foreach(positions => {
      if(positions.contains(person)) positions.remove(person)
    })
    this
  }
}

case class DriversRoadMap(roads: Array[Array[Boolean]]) extends DriversMap
trait DriversMap extends Maps[Driver]

case class PassengersRoadMap(roads: Array[Array[Boolean]]) extends PassengersMap
trait PassengersMap extends Maps[Passenger]
