package model

import play.api.libs.json.Json

case class Route(originPosition: Position, targetPosition: Position)

object Route {
  implicit val jsonFormat = Json.format[Route]

}