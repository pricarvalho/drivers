package services

import fixture.MapFixture
import model.{Driver, Passenger, Position, Route}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CallerSpec extends Specification {

  "Caller" should {

    "call a taxi driver closer with" in {
      "one empty driver on the map" in {
        val mapa = MapFixture.createDriversRoadMap
        mapa.add(Driver(tagCar = "APRIL-2017", Position(10,2), status = 1))

        val passenger = Passenger(Route(Position(0,0), Position(14,7)))
        val result = new Caller(mapa).from(passenger)

        result.isEmpty must beFalse
        result.get.path.size must beEqualTo(13)
      }

      "with two empty driver on the map in the same position" in {
        val mapa = MapFixture.createDriversRoadMap
        mapa.add(Driver(tagCar = "APRIL-2017", Position(10,2), status = 1))
        mapa.add(Driver(tagCar = "APRIL-2018", Position(10,2), status = 1))

        val passenger = Passenger(Route(Position(0,0), Position(14,7)))
        val result = new Caller(mapa).from(passenger)

        result.isEmpty must beFalse
      }

      "with two empty driver on the map in distinct positions" in {
        val mapa = MapFixture.createDriversRoadMap
        val closerDriver = Driver(tagCar = "APRIL-2017", Position(10,2), status = 1)
        mapa.add(closerDriver)
        mapa.add(Driver(tagCar = "APRIL-2018", Position(2,2), status = 1))

        val passenger = Passenger(Route(Position(0,0), Position(14,7)))
        val result = new Caller(mapa).from(passenger)

        result.isEmpty must beFalse
        result.get.driver must beEqualTo(closerDriver)
      }

      "one empty driver on the map in distinct positions" in {
        val mapa = MapFixture.createDriversRoadMap
        val fartherDriver = Driver(tagCar = "APRIL-2018", Position(2,2), status = 1)
        mapa.add(fartherDriver)
        mapa.add(Driver(tagCar = "APRIL-2017", Position(10,2), status = 2))

        val passenger = Passenger(Route(Position(0,0), Position(14,7)))
        val result = new Caller(mapa).from(passenger)

        result.isEmpty must beFalse
        result.get.driver must beEqualTo(fartherDriver)
      }
    }

    "returns None for call a taxi driver that not exists on the map" in {
      val mapa = MapFixture.createDriversRoadMap
      val passenger = Passenger(Route(Position(0,0), Position(14,7)))
      val result = new Caller(mapa).from(passenger)

      result must beEqualTo(None)
    }

  }

}
