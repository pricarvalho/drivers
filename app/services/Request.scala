package services

import model._
import play.api.libs.json.Json

class Request(drivers: DriversMap, passengers: PassengersMap) {

  def to(move: Move): RouteInfo = {
    val evaluatedPath = new Router(drivers).evaluate(move.route).toList.sortBy(_.score)
    val path = move.timeOnThePath(evaluatedPath)
    drivers.movePosition(move.driver, path.head.position).fold(ifEmpty = ???)(driver => {
      val routeInfo = RouteInfo(driver, move.passenger, path)
      updatePersonWith(routeInfo)
    })
  }

  private def updatePersonWith(routeInfo: RouteInfo) = {
    drivers.update(routeInfo.driver)
    if (routeInfo.driverStatus == 3) {
      passengers.movePosition(routeInfo.passenger, routeInfo.driverCurrentPosition)
    }
    routeInfo
  }
}

case class RouteInfo(driver: Driver, passenger: Passenger, currentRoute: Route, path: List[PriorityPosition] = List.empty) {

  def driverCurrentPosition = driver.currentPosition

  def driverStatus = driver.status

  def toMove(time: Int): Option[Move] = {
    if(driverStatus > 1) Some(Move(driver, passenger, currentRoute, time)) else None
  }

}

object RouteInfo {

  implicit val jsonFormat = Json.format[RouteInfo]

  def apply(driver: Driver, passenger: Passenger, path: List[PriorityPosition]): RouteInfo = {
    if(path.length == 1) {
      path.head.position match {
        case passenger.currentPosition => new RouteInfo(driver.copy(status = 3), passenger, Route(passenger.currentPosition, passenger.targetPosition), path)
        case passenger.targetPosition => new RouteInfo(driver.copy(status = 1), passenger, Route(passenger.targetPosition, passenger.targetPosition), path)
      }
    } else {
      RouteInfo(driver, passenger, Route(path.head.position, path.last.position), path)
    }
  }

}

case class Move(driver: Driver, passenger: Passenger, route: Route, time: Int) {

  def timeOnThePath(path: List[PriorityPosition]): List[PriorityPosition] = {
    if(path.size > time)
      path drop (time)
    else path.drop(path.size - 1)
  }

}
