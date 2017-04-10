package services

import javax.inject.Singleton

import model.{Cabby, Position}

import scala.collection.mutable.{HashMap, ListBuffer}
import scala.util.Try


trait RoadMap {

}

@Singleton
case class RoadMapService(roads: Array[Array[Boolean]]) extends RoadMap {

  type DriversByPosition = HashMap[Position, ListBuffer[Cabby]]

  private val cabbies: DriversByPosition = new DriversByPosition

  def unblocked(position: Position): Boolean = {
    Try(roads(position.x)(position.y)).getOrElse(false)
  }

  def findCabbies(position: Position): ListBuffer[Cabby] = {
    cabbies.get(position).filter(_.nonEmpty).fold(ListBuffer.empty[Cabby])(x => x)
  }

  def add(cabby: Cabby): Unit = cabbies.get(cabby.currentPosition)
    .filter(_.nonEmpty)
    .fold(ifEmpty = if(unblocked(cabby.currentPosition)) {
      this.cabbies.put(cabby.currentPosition, ListBuffer(cabby))
    })(cabbies => {
      if(cabbies.contains(cabby)) cabbies -= cabby
      cabbies += cabby
    })

  def update(cabby: Cabby, newPosition: Position): Unit = {
    val filtered = cabbies(cabby.currentPosition).filterNot(_.equals(cabby))
    this.cabbies.updated(cabby.currentPosition, filtered)
    this.add(cabby.copy(currentPosition = newPosition))
  }

}
