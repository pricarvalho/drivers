package model

import play.api.Environment

import scala.collection.mutable.MutableList
import scala.io.Source.fromInputStream


trait RoadMap {

  type DriversByPosition = Map[Position, MutableList[Cabby]]

  val roads: DriversByPosition = {
    val file = Environment.simple().resourceAsStream("cidade.txt").map(fromInputStream(_))
    file.fold(ifEmpty = ???)(file => {
      file.getLines.zipWithIndex.flatMap{ case (lineValue, line) => {
        lineValue.zipWithIndex.map{ case (columnValue, column) if(columnValue.equals(',')) => {
           (Position(line, column), MutableList.empty[Cabby])
        }}
      }}
    }).toMap
  }

  def add(cabby: Cabby): Unit = roads.get(cabby.currentPosition).foreach(cabbies => {
    if(!cabbies.contains(cabby)) cabbies += cabby
  })

  def update(cabby: Cabby, newPosition: Position): Unit = {
    roads.get(cabby.currentPosition).foreach(cabbies  => {
      roads.updated(cabby.currentPosition, cabbies.filterNot(_.equals(cabby)))
    })
    this.add(cabby.copy(currentPosition = newPosition))
  }

}
