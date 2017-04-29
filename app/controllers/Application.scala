package controllers

import javax.inject.Inject

import play.api.mvc._
import play.cache.CacheApi
import services.{CabbiesMap, PassengersMap}

class Application @Inject()(cabbies: CabbiesMap, passengers: PassengersMap, cache: CacheApi) extends Controller {

  def index = Action {
//    Ok(views.html.index("Your new application is ready."))
    Ok
  }

  def restart = Action {
    cabbies.people.clear()
    passengers.people.values.flatten.foreach(passenger => cache.remove(passenger.uuid.toString))
    passengers.people.clear()
    NoContent
  }

}