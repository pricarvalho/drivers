package services

import fixture.MapFixture
import model.{Cabby, Passenger, Position, Route}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CallerSpec extends Specification {

  "Caller" should {

    "call a taxi driver closer with" in {
      "one empty cabby on the map" in {
        val mapa = MapFixture.createCabbiesRoadMap
        mapa.add(Cabby(tagCar = "APRIL-2017", Position(10,2), statusCode = 1))

        val passenger = Passenger(1, Route(Position(0,0), Position(14,7)))
        val result = Caller(mapa).from(passenger)

        result.isEmpty must beFalse
        result.get.path.size must beEqualTo(12)
      }

      "with two empty cabby on the map in the same position" in {
        val mapa = MapFixture.createCabbiesRoadMap
        mapa.add(Cabby(tagCar = "APRIL-2017", Position(10,2), statusCode = 1))
        mapa.add(Cabby(tagCar = "APRIL-2018", Position(10,2), statusCode = 1))

        val passenger = Passenger(1, Route(Position(0,0), Position(14,7)))
        val result = Caller(mapa).from(passenger)

        result.isEmpty must beFalse
      }

      "with two empty cabby on the map in distinct positions" in {
        val mapa = MapFixture.createCabbiesRoadMap
        val closerCabby = Cabby(tagCar = "APRIL-2017", Position(10,2), statusCode = 1)
        mapa.add(closerCabby)
        mapa.add(Cabby(tagCar = "APRIL-2018", Position(2,2), statusCode = 1))

        val passenger = Passenger(1, Route(Position(0,0), Position(14,7)))
        val result = Caller(mapa).from(passenger)

        result.isEmpty must beFalse
        result.get.cabby must beEqualTo(closerCabby)
      }

      "one empty cabby on the map in distinct positions" in {
        val mapa = MapFixture.createCabbiesRoadMap
        val fartherCabby = Cabby(tagCar = "APRIL-2018", Position(2,2), statusCode = 1)
        mapa.add(fartherCabby)
        mapa.add(Cabby(tagCar = "APRIL-2017", Position(10,2), statusCode = 2))

        val passenger = Passenger(1, Route(Position(0,0), Position(14,7)))
        val result = Caller(mapa).from(passenger)

        result.isEmpty must beFalse
        result.get.cabby must beEqualTo(fartherCabby)
      }
    }

    "returns None for call a taxi driver that not exists on the map" in {
      val mapa = MapFixture.createCabbiesRoadMap
      val passenger = Passenger(1, Route(Position(0,0), Position(14,7)))
      val result = Caller(mapa).from(passenger)

      result must beEqualTo(None)
    }

  }

}
