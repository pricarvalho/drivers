package controllers

import java.util.UUID
import javax.inject.Inject

import json.{CabbyMovesRequest, CabbySavesRequest}
import model.{Cabby, Passenger, Route}
import play.api.cache.CacheApi
import play.api.libs.json.Json
import play.api.mvc._
import services.{CabbiesMap, Caller, Move, PassengersMap, Request}

import scala.util.Try

class Cabbies @Inject()(cabbies: CabbiesMap, passengers: PassengersMap, cache: CacheApi) extends Controller {

  def post = Action(parse.json) { request =>
    import model.Cabby._
    val cabbyRequest = Json.fromJson[CabbySavesRequest](request.body).asOpt
    cabbyRequest.fold(BadRequest("Bad formatted json"))(cabbyRequest => {
      cabbies.add(cabbyRequest.toCabby).fold(
        BadRequest("Add cabby error: Cabby not found")
      )(cabby => {
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
            cache.set(passengerUUID, callerAnswered.toRouteInfo)
            Ok(Json.toJson(callerAnswered))
          })
      })
    })
  }

  def move = Action(parse.json) { request =>
    import services.RequestResult._
    val moveRequest = Json.fromJson[CabbyMovesRequest](request.body).asOpt
    moveRequest.fold(BadRequest("Bad formatted json"))(move => {
      cache.get[RouteInfo](move.passenger).fold(
        BadRequest("no driver to move")
      )(routeInfo => {
        val moving = routeInfo.toMove(move.time)
        val requestResult = new Request(cabbies, passengers).to(moving)
        cache.set(move.passenger, requestResult.toRouteInfo)
        Ok(Json.toJson(requestResult))
      })
    })
  }

}

case class RouteInfo(cabby: Cabby, passenger: Passenger, currentRoute: Route) {

  def toMove(time: Int) = Move(cabby, passenger, currentRoute, time)

}
