package services

import model.MoveStatus.{ARRIVED, ON_THE_WAY}
import model._

class Request(cabbies: CabbiesMap, passengers: PassengersMap) {

  def to(move: Move): Option[RequestResult] = {
    val evaluatedPath = new Router(cabbies).evaluate(move.route).toList.reverse
    val path = move.timeOnThePath(evaluatedPath)
    path.headOption.map(priority => {
      val position = priority.position
      cabbies.movePosition(move.cabby, position)
      if(path.size > 1) RequestResult(ON_THE_WAY, path)
      else RequestResult(ARRIVED, path)
    })
  }

}

case class RequestResult(status: MoveStatus, path: List[PriorityPosition])

case class Move(cabby: Cabby, passenger: Passenger, route: Route, time: Int = 1) {

  def isSamePassengerOrigin(position: Position): Boolean = position.equals(passenger.currentPosition)

  def timeOnThePath(path: List[PriorityPosition]): List[PriorityPosition] = {
    if(path.size > time)
      path drop (time)
    else path.drop(path.size - 1)
  }

}
