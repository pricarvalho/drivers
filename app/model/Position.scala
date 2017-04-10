package model

import play.api.libs.json.Json

case class Position(x: Int, y: Int) {

  def neighbors: List[Position] = List(left, right, up, down)

  def left = Position(x, y - 1)

  def right = Position(x, y + 1)

  def up = Position(x + 1, y)

  def down = Position(x - 1, y)

  override def equals(obj: Any): Boolean = obj match {
    case other: Position => this.x.equals(other.x) && this.y.equals(other.y)
    case _ => false
  }

  override def hashCode: Int = x.hashCode + y.hashCode

}

object Position {
  implicit val jsonFormat = Json.format[Position]
}

case class PriorityPosition(counter: Int, position: Position) extends Ordered[PriorityPosition] {

  def neighbors(f: Position => Boolean) = this.position.neighbors.filter(f)

  override def compare(other: PriorityPosition) = other.counter - this.counter

  override def equals(obj: Any): Boolean = obj match {
    case other: PriorityPosition => this.position.equals(other.position)
    case _ => false
  }

  override def hashCode: Int = counter.hashCode() + position.hashCode

}
