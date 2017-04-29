package controllers

import javax.inject.Inject

import json.PassengerSavesRequest
import model.Passenger._
import play.api.libs.json.Json
import play.api.mvc._
import services.PassengersMap

class Passengers @Inject()(passengers: PassengersMap) extends Controller {

  def post = Action (parse.json) { request =>
    val passengerRequest = Json.fromJson[PassengerSavesRequest](request.body).asOpt
    passengerRequest.fold(BadRequest("Bad formatted json"))(passengerRequest => {
      passengers.add(passengerRequest.toPassenger).fold(
        BadRequest("Add passenger error: Passenger not found")
      )(passenger => {
        Created(Json.toJson(passenger))
      })
    })
  }

}
