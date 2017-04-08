package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller, Result}
import services.MapsService

class MapsController @Inject()(mapsService: MapsService) extends Controller {

  def list = Action {
    def books = mapsService.list
    Ok(Json.toJson(books))
  }

}
