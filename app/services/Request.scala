package services

import model._
import play.api.libs.json.Json

class Request(cabbies: CabbiesMap, passengers: PassengersMap) {

  def to(move: Move): RouteInfo = {
    val evaluatedPath = new Router(cabbies).evaluate(move.route).toList.sortBy(_.score)
    val path = move.timeOnThePath(evaluatedPath)
    cabbies.movePosition(move.cabby, path.head.position).fold(ifEmpty = ???)(cabby => {
      val routeInfo = RouteInfo(cabby, move.passenger, path)
      updatePersonWith(routeInfo)
    })
  }

  private def updatePersonWith(routeInfo: RouteInfo) = {
    cabbies.update(routeInfo.cabby)
    if (routeInfo.cabbyStatus == 3) {
      passengers.movePosition(routeInfo.passenger, routeInfo.cabbyCurrentPosition)
    }
    routeInfo
  }
}

case class RouteInfo(cabby: Cabby, passenger: Passenger, currentRoute: Route, path: List[PriorityPosition] = List.empty) {

  def cabbyCurrentPosition = cabby.currentPosition

  def cabbyStatus = cabby.status

  def toMove(time: Int): Option[Move] = {
    if(cabbyStatus > 1) Some(Move(cabby, passenger, currentRoute, time)) else None
  }

}

object RouteInfo {

  implicit val jsonFormat = Json.format[RouteInfo]

  def apply(cabby: Cabby, passenger: Passenger, path: List[PriorityPosition]): RouteInfo = {
    if(path.length == 1) {
      path.head.position match {
        case passenger.currentPosition => new RouteInfo(cabby.copy(status = 3), passenger, Route(passenger.currentPosition, passenger.targetPosition), path)
        case passenger.targetPosition => new RouteInfo(cabby.copy(status = 1), passenger, Route(passenger.targetPosition, passenger.targetPosition), path)
      }
    } else {
      RouteInfo(cabby, passenger, Route(path.head.position, path.last.position), path)
    }
  }

}

case class Move(cabby: Cabby, passenger: Passenger, route: Route, time: Int) {

  def timeOnThePath(path: List[PriorityPosition]): List[PriorityPosition] = {
    if(path.size > time)
      path drop (time)
    else path.drop(path.size - 1)
  }

}
