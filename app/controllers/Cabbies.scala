package controllers

import java.util.UUID
import javax.inject.Inject

import json.CabbySavesRequest
import play.api.libs.json.Json
import play.api.mvc._
import services.{CabbiesMap, Caller, PassengersMap}

import scala.util.Try

class Cabbies @Inject()(cabbies: CabbiesMap, passengers: PassengersMap) extends Controller {

  def post = Action (parse.json) { request =>
    import model.Cabby._
    val cabbyRequest = Json.fromJson[CabbySavesRequest](request.body).asOpt
    cabbyRequest.fold(BadRequest("Bad formatted json"))(cabbyRequest => {
      val id = cabbies.add(cabbyRequest.toCabby)
      cabbies.find(id).fold(BadRequest("Add cabby error: Cabby not found"))(cabby => {
        Created(Json.toJson(cabby))
      })
    })
  }

  def requests(passengerUUID: String) = Action {
    import services.CallerAnswered._
    Try(UUID.fromString(passengerUUID)).toOption.fold(BadRequest("invalid request"))(uuid => {
      passengers.find(uuid).fold(BadRequest("passenger not found"))(passenger => {
        new Caller(cabbies).from(passenger)
          .fold(BadRequest("cabbies not found"))(callerAnswered => {
            Ok(Json.toJson(callerAnswered))
          })
      })
    })
  }

}
