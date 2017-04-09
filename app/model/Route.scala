package model

import play.api.libs.json.Json

case class Route(originPosition: Position, targetPosition: Position) {

  def moveTheDriverOnTime(time: Int) = {

    ???
  }

}

object Route {
  implicit val jsonFormat = Json.format[Route]
}
