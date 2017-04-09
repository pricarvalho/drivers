package model

trait Status {
  val statusCode: Int
}

case class Unappropriated(cabby: Cabby, currentPosition: Position) extends Status {
  override val statusCode = 1
}

case class EnRoute(cabby: Cabby, route: Route) extends Status {
  override val statusCode = 2
}

case class Running(cabby: Cabby, passenger: Passenger) extends Status {
  override val statusCode = 3
}
