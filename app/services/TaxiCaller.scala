package services

import javax.inject.Inject

import model.{Cabby, Passenger, PriorityPosition}

import scala.collection.mutable.{ListBuffer, PriorityQueue}
import scala.util.Try

case class TaxiCaller @Inject()(roadMap: RoadMap) {

  val queuePositions = new PriorityQueue[PriorityPosition]
  val scoringPositions = ListBuffer.empty[PriorityPosition]

  def callFrom(passenger: Passenger): Option[TaxiCallerAnswered] = {

    def findTaxiBy(priorityPosition: PriorityPosition): Cabby = {
      priorityPosition.neighbors(roadMap.unblocked)
        .map(PriorityPosition(priorityPosition.counter + 1, _))
        .filterNot(scoringPositions.contains)
        .map(prioritizePosition)
        .map(priority => roadMap.listIn[Cabby](priority.position)).flatten
        .find(_.empty).fold(ifEmpty = findTaxiBy(queuePositions.dequeue()))(x => x)
    }

    val currentPosition = prioritizePosition(PriorityPosition(0, passenger.currentPosition))
    Try(findTaxiBy(currentPosition)).toOption
      .map(TaxiCallerAnswered(passenger, _, this.scoringPositions.toList))
  }

  private def prioritizePosition(priorityPosition: PriorityPosition): PriorityPosition = {
    this.queuePositions enqueue priorityPosition
    this.scoringPositions += priorityPosition
    priorityPosition
  }

}

case class TaxiCallerAnswered(passenger: Passenger, cabby: Cabby, queue: List[PriorityPosition])
