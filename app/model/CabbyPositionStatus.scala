package model

trait CabbyPositionStatus {
  val statusCode: Int
}

case class Unappropriated(cabby: Cabby, currentPosition: Position) extends CabbyPositionStatus {
  override val statusCode = 1
}

case class EnRoute(cabby: Cabby, route: Route) extends CabbyPositionStatus {
  override val statusCode = 2
}

case class Running(cabby: Cabby, passenger: Passenger) extends CabbyPositionStatus {
  override val statusCode = 3
}
