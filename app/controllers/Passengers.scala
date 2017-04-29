package controllers

import javax.inject.Inject

import json.json.PassengerSavesRequest
import play.api.libs.json.Json
import play.api.mvc._
import services.PassengersMap
import model.Passenger._

class Passengers @Inject()(passengers: PassengersMap) extends Controller {

  def post = Action (parse.json) { request =>
    val passengerRequest = Json.fromJson[PassengerSavesRequest](request.body).asOpt
    passengerRequest.fold(BadRequest("Bad formatted json"))(passengerRequest => {
      val uuid = passengers.add(passengerRequest.toPassenger)
      passengers.find(uuid).fold(BadRequest("Add passenger error: Passenger not found"))(passenger => {
        Created(Json.toJson(passenger))
      })
    })
  }

}
