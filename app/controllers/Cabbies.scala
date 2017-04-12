package controllers

import javax.inject.Inject

import json.CabbySavesRequest
import play.api.libs.json.Json
import play.api.mvc._
import services.CabbiesMap

class Cabbies @Inject()(maps: CabbiesMap) extends Controller {

  def post = Action (parse.json) { request =>
    val cabbyRequest = Json.fromJson[CabbySavesRequest](request.body).asOpt
    cabbyRequest.fold(NotAcceptable)(x => Created)
  }

}
