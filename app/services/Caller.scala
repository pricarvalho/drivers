package services

import model._

import scala.collection.mutable.Set
import scala.util.Try

case class Caller(private val cabbiesMap: CabbiesMap) extends PriorityPositions {

  protected val scorePositions: Array[Array[Int]] = {
    val positions = new Array[Array[Int]](cabbiesMap.roads.size)
    for (i <- 0 until positions.size) positions(i) = new Array[Int](cabbiesMap.roads(i).size)
    positions
  }

  def from(passenger: Passenger): Option[CallerAnswered] = {

    def search(priorityPosition: PriorityPosition): (Cabby, Set[PriorityPosition]) = {
      priorityPosition.neighbors(cabbiesMap unblocked)
        .map(PriorityPosition(priorityPosition.score + 1, _))
        .filterNot(scoredPosition)
        .map(prioritize(_).position)
        .map(position => cabbiesMap list position).flatten
        .find(cabby => cabby.empty)
        .fold(ifEmpty = search(positions dequeue))(cabby => {
          super.score(priorityPosition)
          val router = new Router(cabbiesMap)
          (cabby, router.evaluate(Route(cabby.currentPosition, passenger.currentPosition)))
        })
    }

    val currentPosition = prioritize(PriorityPosition(0, passenger.currentPosition))
    Try(search(super.score(currentPosition))).toOption
      .map{case (cabby, path) => CallerAnswered(passenger, cabby, path.toList)}
  }

}

case class CallerAnswered(passenger: Passenger, cabby: Cabby, path: List[PriorityPosition])
