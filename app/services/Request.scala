package services

import controllers.RouteInfo
import model._
import play.api.libs.json.Json

class Request(cabbies: CabbiesMap, passengers: PassengersMap) {

  def to(move: Move): RequestResult = {
    val evaluatedPath = new Router(cabbies).evaluate(move.route).toList.sortBy(_.score)
    println(evaluatedPath.mkString("\n"))
    val path = move.timeOnThePath(evaluatedPath)
    val position = path.head.position
    cabbies.movePosition(move.cabby, position).fold(ifEmpty = ???)(cabby => {
      RequestResult(cabby, move.passenger, path)
    })
  }

}

case class RequestResult(cabby: Cabby, passenger: Passenger, path: List[PriorityPosition]) {
  def toRouteInfo = {
    if(path.length == 1) {
      path.head.position match {
        case passenger.currentPosition => RouteInfo(cabby, passenger, Route(passenger.currentPosition, passenger.targetPosition))
        case passenger.targetPosition => RouteInfo(cabby, passenger, Route(passenger.targetPosition, passenger.targetPosition))
      }
    } else {
      RouteInfo(cabby, passenger, Route(path.head.position, path.last.position))
    }
  }
}
object RequestResult {
  implicit val jsonFormat = Json.format[RequestResult]
}

case class Move(cabby: Cabby, passenger: Passenger, route: Route, time: Int) {

  def isSamePassengerOrigin(position: Position): Boolean = position.equals(passenger.currentPosition)

  def timeOnThePath(path: List[PriorityPosition]): List[PriorityPosition] = {
    if(path.size > time)
      path drop (time)
    else path.drop(path.size - 1)
  }

}
