package controllers

import java.util.UUID
import javax.inject.Inject

import json.{DriverMovesRequest, DriverSavesRequest}
import play.api.cache.CacheApi
import play.api.libs.json.Json
import play.api.mvc._
import services.{DriversMap, Caller, PassengersMap, RouteInfo, Request}

import scala.util.Try

class Drivers @Inject()(drivers: DriversMap, passengers: PassengersMap, cache: CacheApi) extends Controller {

  def post = Action(parse.json) { request =>
    val driverRequest = Json.fromJson[DriverSavesRequest](request.body).asOpt
    driverRequest.fold(BadRequest("Bad formatted json"))(driverRequest => {
      drivers.add(driverRequest.toDriver).fold(
        BadRequest("Add driver error: Driver not found")
      )(driver => {
        Created(Json.toJson(driver))
      })
    })
  }

  def requests(passengerUUID: String) = Action {
    Try(UUID.fromString(passengerUUID)).toOption.fold(BadRequest("invalid request"))(uuid => {
      passengers.find(uuid).fold(BadRequest("passenger not found"))(passenger => {
        new Caller(drivers).from(passenger)
          .fold(BadRequest("drivers not found"))(callerAnswered => {
            cache.set(passengerUUID, callerAnswered.toRouteInfo)
            Ok(Json.toJson(callerAnswered))
          })
      })
    })
  }

  def move = Action(parse.json) { request =>
    val moveRequest = Json.fromJson[DriverMovesRequest](request.body).asOpt
    moveRequest.fold(BadRequest("Bad formatted json"))(move => {
      cache.get[RouteInfo](move.passenger).fold(
        BadRequest("no driver to move")
      )(routeInfo => {
        routeInfo.toMove(move.time).fold(ifEmpty = NoContent)(moving => {
          val requestResult = new Request(drivers, passengers).to(moving)
          cache.set(move.passenger, requestResult)
          Ok(Json.toJson(requestResult))
        })
      })
    })
  }

}


