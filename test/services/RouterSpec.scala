package services

import fixture.MapFixture
import model.{Cabby, Passenger, Position, Route}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RouterSpec extends Specification {

  "Router" should {

    "evaluated a path from a route with" in {
      "one empty cabby on the map" in {
        val mapa = MapFixture.createCabbiesRoadMap
        val cabby = Cabby(tagCar = "APRIL-2017", Position(10,2), status = 1)
        val passenger = Passenger(1, Route(Position(0,0), Position(14,7)))
        mapa.add(cabby)

        val route = Route(cabby.currentPosition, passenger.currentPosition)
        val result = new Router(mapa).evaluate(route)

        result.isEmpty must beFalse
        result.size must beEqualTo(12)
      }

      "two empty cabbies on the map in the same position" in {
        val mapa = MapFixture.createCabbiesRoadMap
        val cabby = Cabby(tagCar = "APRIL-2017", Position(10,2), status = 1)
        val passenger = Passenger(1, Route(Position(0,0), Position(14,7)))
        mapa.add(Cabby(tagCar = "APRIL-2018", Position(10,2), status = 1))
        mapa.add(cabby)

        val route = Route(cabby.currentPosition, passenger.currentPosition)
        val result = new Router(mapa).evaluate(route)

        result.isEmpty must beFalse
        result.size must beEqualTo(12)
      }
//
//      "with two empty cabby on the map in distinct positions" in {
//        val mapa = MapFixture.createCabbiesRoadMap
//        val closerCabby = Cabby(tagCar = "APRIL-2017", Position(10,2), statusCode = 1)
//        mapa.add(closerCabby)
//        mapa.add(Cabby(tagCar = "APRIL-2018", Position(2,2), statusCode = 1))
//
//        val passenger = Passenger(1, Route(Position(0,0), Position(14,7)))
//        val result = new Caller(mapa).from(passenger)
//
//        result.isEmpty must beFalse
//        result.get.cabby must beEqualTo(closerCabby)
//      }
//
//      "one empty cabby on the map in distinct positions" in {
//        val mapa = MapFixture.createCabbiesRoadMap
//        val fartherCabby = Cabby(tagCar = "APRIL-2018", Position(2,2), statusCode = 1)
//        mapa.add(fartherCabby)
//        mapa.add(Cabby(tagCar = "APRIL-2017", Position(10,2), statusCode = 2))
//
//        val passenger = Passenger(1, Route(Position(0,0), Position(14,7)))
//        val result = new Caller(mapa).from(passenger)
//
//        result.isEmpty must beFalse
//        result.get.cabby must beEqualTo(fartherCabby)
//      }
    }

  }

}
