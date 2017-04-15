package services

import model.{Position, PriorityPosition}

import scala.collection.mutable.PriorityQueue
import scala.util.Try

trait PriorityPositions {

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
