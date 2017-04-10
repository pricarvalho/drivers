package services

import javax.inject.Inject

import model.{Cabby, Passenger, Position, PriorityPosition}

import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.ListBuffer

case class TaxiCaller @Inject()(roadMap: RoadMap) {

  val queuePositions = new PriorityQueue[PriorityPosition]
  val scoringPositions = ListBuffer.empty[PriorityPosition]

  def callFrom(passenger: Passenger) = {

    def findTaxiBy(priorityPosition: PriorityPosition): Option[Cabby] = {
      priorityPosition.neighbors
        .filter(p => roadMap.isUnblocked(p) && isPositionPrioritized(priorityPosition, p))
        .map(roadMap.find(_)).flatten.headOption
        .fold(ifEmpty = findTaxiBy(queuePositions.dequeue()))(_.headOption)
    }

    val currentPosition = PriorityPosition(0, passenger.currentPosition)
    scoringPositions += currentPosition
    findTaxiBy(currentPosition).map(TaxiCallerAnswered(passenger, _, this.scoringPositions.toList))
  }

  private def isPositionPrioritized(priorityPosition: PriorityPosition, neighbor: Position): Boolean = {
    val position = PriorityPosition(priorityPosition.counter + 1, neighbor)
    scoringPositions.find(_.equals(position)).fold(ifEmpty = {
      this.queuePositions enqueue position
      this.scoringPositions += position
      true
    })(_ => false)
  }

}

case class TaxiCallerAnswered(passenger: Passenger, cabby: Cabby, queue: List[PriorityPosition])
