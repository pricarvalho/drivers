package controllers

import javax.inject.Inject

import json.json.PassengerSavesRequest
import play.api.libs.json.Json
import play.api.mvc._
import services.Maps

class Passengers @Inject()(maps: Maps) extends Controller {

  def post = Action (parse.json) { request =>
    val passengerRequest = Json.fromJson[PassengerSavesRequest](request.body).asOpt
    passengerRequest.fold(NotAcceptable)(x => Created)
  }

}
