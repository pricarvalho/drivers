package services

import model.{PriorityPosition, Route}

import scala.collection.mutable.Set

class Router(private val drivers: DriversMap) extends PriorityPositions {

  override protected val roads: Array[Array[Boolean]] = drivers.roads

  def evaluate(route: Route): Set[PriorityPosition] = {
    val currentPosition = prioritize(PriorityPosition(1, route.originPosition))
    val path = Set.empty[PriorityPosition]

    def search(priorityPosition: PriorityPosition): Set[PriorityPosition] = {
      priorityPosition.neighbors(drivers unblocked)
        .map(PriorityPosition(priorityPosition.score + 1, _))
        .filterNot(scoredPosition)
        .map(prioritize)
        .find(_.equals(route.targetPosition))
        .fold(ifEmpty = search(positions dequeue))(targetPosition => {
          val scoredPosition = super.score(targetPosition)
          path.add(scoredPosition)
          discoverRoute(scoredPosition)
        })
    }

    def discoverRoute(priorityPosition: PriorityPosition): Set[PriorityPosition] = {
      priorityPosition.neighbors(drivers unblocked)
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
