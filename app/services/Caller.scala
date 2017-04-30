package services

import model._
import play.api.libs.json.Json

import scala.collection.mutable.Set
import scala.util.Try

class Caller(private val drivers: DriversMap) extends PriorityPositions {

  override protected val roads: Array[Array[Boolean]] = drivers.roads

  def from(passenger: Passenger): Option[CallerAnswered] = {

    def search(priorityPosition: PriorityPosition): (Driver, Set[PriorityPosition]) = {
      priorityPosition.neighbors(drivers unblocked)
        .map(PriorityPosition(priorityPosition.score + 1, _))
        .filterNot(scoredPosition)
        .map(prioritize(_).position)
        .map(position => drivers list position).flatten
        .find(driver => driver.empty)
        .fold(ifEmpty = search(positions dequeue))(driver => {
          super.score(priorityPosition)
          val router = new Router(drivers)
          (driver, router.evaluate(Route(driver.currentPosition, passenger.currentPosition)))
        })
    }

    val currentPosition = prioritize(PriorityPosition(0, passenger.currentPosition))
    Try(search(super.score(currentPosition))).toOption
      .map{case (driver, path) => CallerAnswered(passenger, driver, path.toList)}
  }

}

case class CallerAnswered(passenger: Passenger, driver: Driver, path: List[PriorityPosition]) {

  def toRouteInfo = RouteInfo(driver.copy(status = 2), passenger, Route(driver.currentPosition, passenger.currentPosition))

}

object CallerAnswered {
  implicit val jsonFormat = Json.format[CallerAnswered]
}
