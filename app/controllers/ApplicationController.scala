package controllers

import play.api.mvc._

class ApplicationController extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}