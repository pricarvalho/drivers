package services

import model.{Cabby, Person, Position}

import scala.collection.mutable.{HashMap, ListBuffer}
import scala.util.Try

trait Maps {

  def unblocked(position: Position): Boolean
  def find[A](position: Position): ListBuffer[A]
  def add(cabby: Cabby): Unit
  def update(cabby: Cabby, newPosition: Position): Unit
}

case class RoadMap(roads: Array[Array[Boolean]]) extends Maps {

  type PeopleByPosition = HashMap[Position, ListBuffer[Person]]

  private val people: PeopleByPosition = new PeopleByPosition

  def unblocked(position: Position): Boolean = {
    Try(roads(position.x)(position.y)).getOrElse(false)
  }

  def find[A](position: Position): ListBuffer[A] = {
    people.get(position).filter(_.nonEmpty).fold(ListBuffer.empty[A])(x => x.asInstanceOf[ListBuffer[A]])
  }

  def add(cabby: Cabby): Unit = people.get(cabby.currentPosition)
    .filter(_.nonEmpty)
    .fold(ifEmpty = if(unblocked(cabby.currentPosition)) {
      this.people.put(cabby.currentPosition, ListBuffer(cabby))
    })(cabbies => {
      if(cabbies.contains(cabby)) cabbies -= cabby
      cabbies += cabby
    })

  def update(cabby: Cabby, newPosition: Position): Unit = {
    val filtered = people(cabby.currentPosition).filterNot(_.equals(cabby))
    this.people.updated(cabby.currentPosition, filtered)
    this.add(cabby.copy(currentPosition = newPosition))
  }

}
