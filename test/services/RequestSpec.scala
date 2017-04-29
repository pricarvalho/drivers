package services

import fixture.MapFixture
import model._
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RequestSpec extends Specification {

  "Request" should {

    "move cabby" in {
      "in time 1 on the path" in {
        val cabbies = MapFixture.createCabbiesRoadMap
        val cabby = Cabby(tagCar = "APRIL-2017", Position(10, 2), status = 1)
        cabbies.add(cabby)
        val passengers = MapFixture.createPassengersMap

        val passenger = Passenger(id = 1, Route(Position(0,0),Position(12,2)))

        val move = Move(cabby, passenger, Route(Position(10, 1), Position(0,0)))
        val subject = new Request(cabbies, passengers)
        val result = subject.to(move)

        result.isEmpty must beFalse
        result.get.path.size must beEqualTo(11)
        result.get.status must beEqualTo(MoveStatus.ON_THE_WAY)
      }

      "in all time on the path" in {
        val cabbies = MapFixture.createCabbiesRoadMap
        val cabby = Cabby(tagCar = "APRIL-2017", Position(10, 2), status = 1)
        cabbies.add(cabby)
        val passengers = MapFixture.createPassengersMap

        val passenger = Passenger(id = 1, Route(Position(0,0),Position(12,2)))

        val move = Move(cabby, passenger, Route(Position(10, 1), Position(0,0)), time = 12)
        val subject = new Request(cabbies, passengers)
        val result = subject.to(move)

        result.isEmpty must beFalse
        result.get.path.size must beEqualTo(1)
        result.get.status must beEqualTo(MoveStatus.ARRIVED)
      }

      "in a greater time on the path" in {
        val cabbies = MapFixture.createCabbiesRoadMap
        val cabby = Cabby(tagCar = "APRIL-2017", Position(10, 2), status = 1)
        cabbies.add(cabby)
        val passengers = MapFixture.createPassengersMap

        val passenger = Passenger(id = 1, Route(Position(0,0),Position(12,2)))

        val move = Move(cabby, passenger, Route(Position(10, 1), Position(0,0)), time = 100)
        val subject = new Request(cabbies, passengers)
        val result = subject.to(move)

        result.isEmpty must beFalse
        result.get.path.size must beEqualTo(1)
        result.get.status must beEqualTo(MoveStatus.ARRIVED)
      }
    }

  }

}
