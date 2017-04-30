package services

import fixture.MapFixture
import model._
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RequestSpec extends Specification {

  "Request" should {

    "move driver" in {
      "in time 1 on the path" in {
        val drivers = MapFixture.createDriversRoadMap
        val driver = Driver(tagCar = "APRIL-2017", Position(10, 2), status = 1)
        drivers.add(driver)
        val passengers = MapFixture.createPassengersMap

        val passenger = Passenger(Route(Position(0,0),Position(12,2)))

        val move = Move(driver, passenger, Route(Position(10, 1), Position(0,0)), time = 1)
        val subject = new Request(drivers, passengers)
        val result = subject.to(move)

        result.path.size must beEqualTo(11)
      }

      "in all time on the path" in {
        val drivers = MapFixture.createDriversRoadMap
        val driver = Driver(tagCar = "APRIL-2017", Position(10, 2), status = 1)
        drivers.add(driver)
        val passengers = MapFixture.createPassengersMap

        val passenger = Passenger(Route(Position(0,0),Position(12,2)))

        val move = Move(driver, passenger, Route(Position(10, 1), Position(0,0)), time = 12)
        val subject = new Request(drivers, passengers)
        val result = subject.to(move)

        result.path.size must beEqualTo(1)
      }

      "in a greater time on the path" in {
        val drivers = MapFixture.createDriversRoadMap
        val driver = Driver(tagCar = "APRIL-2017", Position(10, 2), status = 1)
        drivers.add(driver)
        val passengers = MapFixture.createPassengersMap

        val passenger = Passenger(Route(Position(0,0),Position(12,2)))

        val move = Move(driver, passenger, Route(Position(10, 1), Position(0,0)), time = 100)
        val subject = new Request(drivers, passengers)
        val result = subject.to(move)

        result.path.size must beEqualTo(1)
      }
    }

  }

}
