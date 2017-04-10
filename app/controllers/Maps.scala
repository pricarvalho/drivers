package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.RoadMap

class Maps @Inject()(roadMap: RoadMap) extends Controller {

  def list = Action {
    def books = "" //mapsService.road
    Ok(Json.toJson(books))
  }

}
