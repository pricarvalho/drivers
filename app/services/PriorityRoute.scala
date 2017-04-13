package services

import model.{Cabby, Passenger, PriorityPosition, Route}

import scala.collection.mutable.{ListBuffer, PriorityQueue}
import scala.util.Try

trait PriorityRoute {

  val positions = new PriorityQueue[PriorityPosition]
  val scorePositions = ListBuffer.empty[PriorityPosition]

  protected def prioritize(priorityPosition: PriorityPosition): PriorityPosition = {
    this.positions enqueue priorityPosition
    this.scorePositions += priorityPosition
    priorityPosition
  }

}

case class Router(cabbiesMap: CabbiesMap, passengersMap: PassengersMap) extends PriorityRoute {

  def evaluate(route: Route): Unit = {

    def find(priorityPosition: PriorityPosition): PriorityPosition = {
      priorityPosition.neighbors(cabbiesMap unblocked)
        .map(PriorityPosition(priorityPosition.counter + 1, _))
        .filterNot(scorePositions.contains)
        .map(position => prioritize(position))
        .find(prioritedPosition => prioritedPosition.equals(route.targetPosition))
        .fold(ifEmpty = find(positions dequeue))(prioritize(_))
    }

    val currentPosition = prioritize(PriorityPosition(0, route.originPosition))
    Try(find(currentPosition)).toOption
  }

}

case class Caller(roadMap: CabbiesMap) extends PriorityRoute {

  def from(passenger: Passenger): Option[CallerAnswered] = {

    def findTaxiBy(priorityPosition: PriorityPosition): Cabby = {
      priorityPosition.neighbors(roadMap unblocked)
        .map(PriorityPosition(priorityPosition.counter + 1, _))
        .filterNot(scorePositions.contains)
        .map(prioritize(_).position)
        .map(position => roadMap list position).flatten
        .find(cabby => cabby.empty)
        .fold(ifEmpty = findTaxiBy(positions dequeue))(cabby => {
          this.scorePositions += priorityPosition
          cabby
        })
    }

    val currentPosition = prioritize(PriorityPosition(0, passenger.currentPosition))
    Try(findTaxiBy(currentPosition)).toOption
      .map(CallerAnswered(passenger, _, scorePositions.reverse.toList))
  }

}

case class CallerAnswered(passenger: Passenger, cabby: Cabby, traveledRoute: List[PriorityPosition])
