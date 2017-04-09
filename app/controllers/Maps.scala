package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

class Maps () extends Controller {

  def list = Action {
    def books = "" //mapsService.road
    Ok(Json.toJson(books))
  }

}
