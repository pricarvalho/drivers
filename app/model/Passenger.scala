package model

case class Passenger(id: Long, route: Route) {

  def currentPosition: Position = route.originPosition

}
