package controllers

import javax.inject.Inject

import play.api.mvc._
import play.cache.CacheApi
import services.{DriversMap, PassengersMap}

class Application @Inject()(drivers: DriversMap, passengers: PassengersMap, cache: CacheApi) extends Controller {

  def index = Action {
//    Ok(views.html.index("Your new application is ready."))
    Ok
  }

  def restart = Action {
    drivers.people.clear()
    passengers.people.values.flatten.foreach(passenger => cache.remove(passenger.uuid.toString))
    passengers.people.clear()
    NoContent
  }

}