package controllers

import json.json.PassengerSavesRequest
import play.api.libs.json.Json
import play.api.mvc._

class Passengers extends Controller {

  def post = Action (parse.json) { request =>
    val passengerRequest = Json.fromJson[PassengerSavesRequest](request.body).asOpt
    passengerRequest.fold(NotAcceptable)(x => Created)
  }

}
