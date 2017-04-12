package services

import model.{Cabby, Passenger, Person, Position}

import scala.collection.mutable.{HashMap, Set => MutableSet}
import scala.util.Try

trait Maps[T <: Person] {

  val roads: Array[Array[Boolean]]
  val people: HashMap[Position, MutableSet[T]]

  def unblocked(position: Position): Boolean = {
    Try(roads(position.x)(position.y)).getOrElse(false)
  }

  def list(position: Position): Set[T]

  def update(person: T, newPosition: Position)

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
  override val people = new HashMap[Position, MutableSet[Cabby]]

  override def list(position: Position): Set[Cabby] = {
    people.get(position).filter(_.nonEmpty).fold(Set.empty[Cabby])(x => x.toSet)
  }

  override def update(cabby: Cabby, newPosition: Position): Unit = {
    val filtered = people(cabby.currentPosition).filterNot(_.equals(cabby))
    this.people.updated(cabby.currentPosition, filtered)
    this.add(cabby.copy(currentPosition = newPosition))
  }
}

case class PassengersRoadMap(roads: Array[Array[Boolean]]) extends PassengersMap
trait PassengersMap extends Maps[Passenger] {
  override val people = new HashMap[Position, MutableSet[Passenger]]

  override def list(position: Position): Set[Passenger] = {
    people.get(position).filter(_.nonEmpty).fold(Set.empty[Passenger])(x => x.toSet)
  }

  override def update(passenger: Passenger, newPosition: Position): Unit = {
    val filtered = people(passenger.currentPosition).filterNot(_.equals(passenger))
    this.people.updated(passenger.currentPosition, filtered)
    this.add(passenger.copy(currentPosition = newPosition))
  }
}
