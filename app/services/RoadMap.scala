package services

import java.io.FileNotFoundException
import javax.inject.Singleton

import model.{Cabby, Position}
import play.api.Environment

import scala.collection.mutable.{HashMap, ListBuffer}
import scala.io.Source.fromInputStream
import scala.util.Try

@Singleton
trait RoadMap {

  type DriversByPosition = HashMap[Position, ListBuffer[Cabby]]

  private val roads: Array[Array[Boolean]] = {
    val file = Environment.simple().resourceAsStream("cidade.txt").map(fromInputStream(_))
    file.fold(ifEmpty = throw new FileNotFoundException)(file => {
      file.getLines().map(linha =>
        linha.map(_.equals(',')).toArray
      ).toArray
    })
  }

  private val cabbies: DriversByPosition = new DriversByPosition

  def isUnblocked(position: Position): Boolean = {
    Try(roads(position.x)(position.y)).getOrElse(false)
  }

  def find(position: Position): Option[ListBuffer[Cabby]] = {
    Try(cabbies.get(position).filter(_.nonEmpty).head).toOption
  }

  def add(cabby: Cabby): Unit = this.find(cabby.currentPosition).fold(ifEmpty = {
    if(isUnblocked(cabby.currentPosition)) this.cabbies.put(cabby.currentPosition, ListBuffer(cabby))
  })(cabbies => {
    if(cabbies.contains(cabby)) cabbies -= cabby
    cabbies += cabby
  })

  def update(cabby: Cabby, newPosition: Position): Unit = {
    this.find(cabby.currentPosition).foreach(cabbies  => {
      this.cabbies.updated(cabby.currentPosition, cabbies.filterNot(_.equals(cabby)))
    })
    this.add(cabby.copy(currentPosition = newPosition))
  }

}
