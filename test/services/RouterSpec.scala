package services

import fixture.MapFixture
import model.{Driver, Passenger, Position, Route}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RouterSpec extends Specification {

  "Router" should {

    "evaluated a path from a route with" in {
      "one empty driver on the map" in {
        val mapa = MapFixture.createDriversRoadMap
        val driver = Driver(tagCar = "APRIL-2017", Position(10,2), status = 1)
        val passenger = Passenger(Route(Position(0,0), Position(14,7)))
        mapa.add(driver)

        val route = Route(driver.currentPosition, passenger.currentPosition)
        val result = new Router(mapa).evaluate(route)

        result.isEmpty must beFalse
        result.size must beEqualTo(13)
      }

      "two empty drivers on the map in the same position" in {
        val mapa = MapFixture.createDriversRoadMap
        val driver = Driver(tagCar = "APRIL-2017", Position(10,2), status = 1)
        val passenger = Passenger(Route(Position(0,0), Position(14,7)))
        mapa.add(Driver(tagCar = "APRIL-2018", Position(10,2), status = 1))
        mapa.add(driver)

        val route = Route(driver.currentPosition, passenger.currentPosition)
        val result = new Router(mapa).evaluate(route)

        result.isEmpty must beFalse
        result.size must beEqualTo(13)
      }

    }

  }

}
