package services

import model.{Cabby, Passenger, PriorityPosition, Route}

import scala.collection.mutable.{ListBuffer, PriorityQueue}
import scala.util.Try

case class TaxiCaller (roadMap: CabbiesMap) {

  val positions = new PriorityQueue[PriorityPosition]
  val scoresByPositions = ListBuffer.empty[PriorityPosition]

  def callFrom(passenger: Passenger): Option[TaxiCallerAnswered] = {

    def findTaxiBy(priorityPosition: PriorityPosition): Cabby = {
      priorityPosition.neighbors(roadMap unblocked)
        .map(PriorityPosition(priorityPosition.counter + 1, _))
        .filterNot(scoresByPositions.contains)
        .map(prioritizePosition(_).position)
        .map(position => roadMap list position).flatten
        .find(_.empty).fold(ifEmpty = findTaxiBy(positions dequeue))(cabby => {
        this.scoresByPositions += priorityPosition
        cabby
      })
    }

    val currentPosition = prioritizePosition(PriorityPosition(0, passenger.currentPosition))
    Try(findTaxiBy(currentPosition)).toOption
      .map(TaxiCallerAnswered(passenger, _, this.scoresByPositions.reverse.toList))
  }

  private def prioritizePosition(priorityPosition: PriorityPosition): PriorityPosition = {
    this.positions enqueue priorityPosition
    this.scoresByPositions += priorityPosition
    priorityPosition
  }

}

case class Router(roadMap: CabbiesMap) {

  val positions = new PriorityQueue[PriorityPosition]
  val scoresByPosition = ListBuffer.empty[PriorityPosition]

  def evaluate(route: Route): Unit = {

    def find(priorityPosition: PriorityPosition): PriorityPosition = {
      priorityPosition.neighbors(roadMap unblocked)
        .map(PriorityPosition(priorityPosition.counter + 1, _))
        .filterNot(scoresByPosition.contains)
        .map(prioritizePosition)
        .find(_.equals(route.targetPosition))
        .fold(ifEmpty = find(positions.dequeue()))(prioritizePosition(_))
    }

    val currentPosition = prioritizePosition(PriorityPosition(0, route.originPosition))
    Try(find(currentPosition)).toOption
  }

  private def prioritizePosition(priorityPosition: PriorityPosition): PriorityPosition = {
    this.positions enqueue priorityPosition
    this.scoresByPosition += priorityPosition
    priorityPosition
  }
}

case class TaxiCallerAnswered(passenger: Passenger, cabby: Cabby, traveledRoute: List[PriorityPosition])
