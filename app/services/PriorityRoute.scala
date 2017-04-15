package services

import model._

import scala.collection.mutable.{Set, PriorityQueue}
import scala.util.Try

trait PriorityRoute {

  protected val positions = new PriorityQueue[PriorityPosition]
  protected val scorePositions: Array[Array[Int]]

  protected def prioritize(priorityPosition: PriorityPosition): PriorityPosition = {
    this.positions enqueue priorityPosition
    this.score(priorityPosition)
  }

  protected def score(priorityPosition: PriorityPosition): PriorityPosition = {
    val position = priorityPosition.position
    this.scorePositions(position.x)(position.y) = priorityPosition.score
    priorityPosition
  }

  protected def scoredPosition(priorityPosition: PriorityPosition): Boolean = {
    val position = priorityPosition.position
    Try(this.scorePositions(position.x)(position.y)).filter(_ > 0).isSuccess
  }

  protected def positionCloser(currentScore: Int, targetPosition: Position): Boolean = {
    Try(this.scorePositions(targetPosition.x)(targetPosition.y))
      .filter(_ == currentScore - 1).isSuccess
  }

}

case class Router(private val cabbiesMap: CabbiesMap) extends PriorityRoute {

  protected val scorePositions: Array[Array[Int]] = {
    val positions = new Array[Array[Int]](cabbiesMap.roads.size)
    for (i <- 0 until positions.size) positions(i) = new Array[Int](cabbiesMap.roads(i).size)
    positions
  }

  def evaluate(route: Route): Set[PriorityPosition] = {
    val currentPosition = prioritize(PriorityPosition(1, route.originPosition))
    val path = Set.empty[PriorityPosition]

    def search(priorityPosition: PriorityPosition): Set[PriorityPosition] = {
      priorityPosition.neighbors(cabbiesMap unblocked)
        .map(PriorityPosition(priorityPosition.score + 1, _))
        .filterNot(scoredPosition)
        .map(prioritize)
        .find(_.equals(route.targetPosition))
        .fold(ifEmpty = search(positions dequeue))(targetPosition => {
          super.score(targetPosition)
          discoverRoute(targetPosition)
        })
    }

    def discoverRoute(priorityPosition: PriorityPosition): Set[PriorityPosition] = {
      priorityPosition.neighbors(cabbiesMap unblocked)
        .find(positionCloser(priorityPosition.score, _))
        .fold(ifEmpty = throw new RuntimeException("erroooooo"))(position => {
          val neighbor = PriorityPosition(priorityPosition.score - 1, position)
          path.add(neighbor)
          if(neighbor.equals(route.originPosition)) path
          else discoverRoute(neighbor)
        })
    }

    search(super.score(currentPosition))
  }

}

case class Caller(private val cabbiesMap: CabbiesMap) extends PriorityRoute {

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
