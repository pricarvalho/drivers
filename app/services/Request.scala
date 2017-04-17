package services

import model.MoveStatus.{ARRIVED, ON_THE_WAY}
import model.{Cabby, MoveStatus, PriorityPosition, Route}

import scala.collection.mutable.Set

class Request(cabbies: CabbiesMap) {

  def to(move: Move): Option[MoveStatus] = {
    val evaluatedPath = new Router(cabbies).evaluate(move.route)
    val path = move.timeOnThePath(evaluatedPath)
    path.headOption.map(priority => {
      cabbies.movePosition(move.cabby, priority.position)
      if(path.size > 1) ON_THE_WAY else ARRIVED
    })
  }

}

case class Move(cabby: Cabby,  route: Route, time: Int = 1) {

  def timeOnThePath(path: Set[PriorityPosition]): Set[PriorityPosition] = {
    if(path.size > time)
      path drop ((path.size - time) - 1)
    else path.drop(0)
  }

}
