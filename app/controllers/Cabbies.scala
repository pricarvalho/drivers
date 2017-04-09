package controllers

import json.CabbySavesRequest
import play.api.libs.json.Json
import play.api.mvc._

class Cabbies extends Controller {

  def post = Action (parse.json) { request =>
    val cabbyRequest = Json.fromJson[CabbySavesRequest](request.body).asOpt
    cabbyRequest.fold(NotAcceptable)(x => Created)
  }

}
