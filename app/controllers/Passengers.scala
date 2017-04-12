package controllers

import javax.inject.Inject

import json.json.PassengerSavesRequest
import play.api.libs.json.Json
import play.api.mvc._
import services.PassengersMap

class Passengers @Inject()(maps: PassengersMap) extends Controller {

  def post = Action (parse.json) { request =>
    val passengerRequest = Json.fromJson[PassengerSavesRequest](request.body).asOpt
    passengerRequest.fold(NotAcceptable)(x => Created)
  }

}
