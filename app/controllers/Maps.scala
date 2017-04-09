package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller, Result}
import services.MapsManager

class Maps @Inject()(mapsService: MapsManager) extends Controller {

  def list = Action {
    def books = mapsService.road
    Ok(Json.toJson(books))
  }

}
