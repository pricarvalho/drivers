package services

import model.{Cabby, Passenger, PriorityPosition, Route}

import scala.collection.mutable.{ListBuffer, PriorityQueue}
import scala.util.Try

case class TaxiCaller (roadMap: RoadMap) {

  val queuePositions = new PriorityQueue[PriorityPosition]
  val scoringPositions = ListBuffer.empty[PriorityPosition]

  def callFrom(passenger: Passenger): Option[TaxiCallerAnswered] = {

    def findTaxiBy(priorityPosition: PriorityPosition): Cabby = {
      priorityPosition.neighbors(roadMap.unblocked)
        .map(PriorityPosition(priorityPosition.counter + 1, _))
        .filterNot(scoringPositions.contains)
        .map(prioritizePosition(_).position)
        .map(roadMap.listIn[Cabby](_)).flatten
        .find(_.empty).fold(ifEmpty = findTaxiBy(queuePositions.dequeue()))(cabby => {
        this.scoringPositions += priorityPosition
        cabby
      })
    }

    val currentPosition = prioritizePosition(PriorityPosition(0, passenger.currentPosition))
    Try(findTaxiBy(currentPosition)).toOption
      .map(TaxiCallerAnswered(passenger, _, this.scoringPositions.toList.reverse))
  }

  private def prioritizePosition(priorityPosition: PriorityPosition): PriorityPosition = {
    this.queuePositions enqueue priorityPosition
    this.scoringPositions += priorityPosition
    priorityPosition
  }

}

case class Router(roadMap: RoadMap) {

  val queuePositions = new PriorityQueue[PriorityPosition]
  val scoringPositions = ListBuffer.empty[PriorityPosition]

  def evaluate(route: Route): Unit = {

    def find(priorityPosition: PriorityPosition): PriorityPosition = {
      priorityPosition.neighbors(roadMap.unblocked)
        .map(PriorityPosition(priorityPosition.counter + 1, _))
        .filterNot(scoringPositions.contains)
        .map(prioritizePosition)
        .find(_.equals(route.targetPosition))
        .fold(ifEmpty = find(queuePositions.dequeue()))(prioritizePosition(_))
    }

    val currentPosition = prioritizePosition(PriorityPosition(0, route.originPosition))
    Try(find(currentPosition)).toOption
  }

  private def prioritizePosition(priorityPosition: PriorityPosition): PriorityPosition = {
    this.queuePositions enqueue priorityPosition
    this.scoringPositions += priorityPosition
    priorityPosition
  }
}

case class TaxiCallerAnswered(passenger: Passenger, cabby: Cabby, queue: List[PriorityPosition])
