package controllers

import javax.inject.Inject

import json.CabbySavesRequest
import play.api.libs.json.Json
import play.api.mvc._
import services.CabbiesMap
import model.Cabby._

class Cabbies @Inject()(cabbies: CabbiesMap) extends Controller {

  def post = Action (parse.json) { request =>
    val cabbyRequest = Json.fromJson[CabbySavesRequest](request.body).asOpt
    cabbyRequest.fold(BadRequest("Bad formatted json"))(cabbyRequest => {
      val id = cabbies.add(cabbyRequest.toCabby)
      cabbies.find(id).fold(BadRequest("Add cabby error: Cabby not found"))(cabby => {
        Created(Json.toJson(cabby))
      })
    })
  }

}
