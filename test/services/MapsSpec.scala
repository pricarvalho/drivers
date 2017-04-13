package services

import fixture.MapFixture
import model.{Cabby, Passenger, Position, Route}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
@RunWith(classOf[JUnitRunner])
class MapsSpec extends Specification {

  "CabbiesMap" should {

    "add and list" in {

      "two different cabbies in the same positions" in {
        val subject = MapFixture.createCabbiesRoadMap
        val position = Position(10, 2)
        subject.add(Cabby(tagCar = "APRIL-2017", position, statusCode = 1))
        subject.add(Cabby(tagCar = "APRIL-2018", position, statusCode = 1))

        val result = subject list position
        result.isEmpty must beFalse
        result.size must beEqualTo(2)
      }

      "two cabbies for each two different positions" in {
        val subject = MapFixture.createCabbiesRoadMap
        val firstPosition = Position(10, 2)
        val secondPosition = Position(2, 2)
        subject.add(Cabby(tagCar = "APRIL-2017", firstPosition, statusCode = 1))
        subject.add(Cabby(tagCar = "APRIL-2018", secondPosition, statusCode = 1))

        val firstResult = subject list firstPosition
        firstResult.isEmpty must beFalse
        firstResult.size must beEqualTo(1)

        val secondResult = subject list secondPosition
        secondResult.isEmpty must beFalse
        secondResult.size must beEqualTo(1)
      }

    }

    "move" in {

      "one cabbie to another position" in {
        val subject = MapFixture.createCabbiesRoadMap

        val cabby = Cabby(tagCar = "APRIL-2017", Position(10, 2), statusCode = 1)
        subject.add(cabby)
        val addCabby = subject.list(cabby.currentPosition).find(_.equals(cabby))
        addCabby.head must beEqualTo(cabby)

        val updatableCabby = addCabby.head.copy(currentPosition = Position(12, 2))
        subject.movePosition(addCabby.head, updatableCabby.currentPosition)
        val updatedCabby = subject.list(updatableCabby.currentPosition).find(_.equals(cabby))
        updatedCabby.isEmpty must beFalse
        updatedCabby.head must beEqualTo(updatableCabby)

        val removedCabby = subject.list(cabby.currentPosition).find(_.equals(cabby))
        removedCabby.isEmpty must beTrue;
      }
    }

  }

  "PassengersMap" should {

    "add and list" in {

      "two different cabbies in the same positions" in {
        val subject = MapFixture.createPassengersMap
        val position = Position(10, 2)
        subject.add(Passenger(id = 1, Route(position,Position(12,2))))
        subject.add(Passenger(id = 2, Route(position,Position(12,2))))

        val result = subject list position
        result.isEmpty must beFalse
        result.size must beEqualTo(2)
      }

      "two cabbies for each two different positions" in {
        val subject = MapFixture.createPassengersMap
        val firstPosition = Position(10, 2)
        val secondPosition = Position(2, 2)
        subject.add(Passenger(id = 1, Route(firstPosition,Position(12,2))))
        subject.add(Passenger(id = 2, Route(secondPosition,Position(12,2))))

        val firstResult = subject list firstPosition
        firstResult.isEmpty must beFalse
        firstResult.size must beEqualTo(1)

        val secondResult = subject list secondPosition
        secondResult.isEmpty must beFalse
        secondResult.size must beEqualTo(1)
      }

    }

    "move" in {

      "one passenger to another position" in {
        val subject = MapFixture.createPassengersMap

        val passenger = Passenger(id = 1, Route(Position(0,0),Position(12,2)))
        subject.add(passenger)
        val addCabby = subject.list(passenger.currentPosition).find(_.equals(passenger))
        addCabby.head must beEqualTo(passenger)

        val updatableCabby = addCabby.head.copy(currentPosition = Position(2, 0))
        subject.movePosition(addCabby.head, updatableCabby.currentPosition)
        val updatedCabby = subject.list(updatableCabby.currentPosition).find(_.equals(passenger))
        updatedCabby.isEmpty must beFalse
        updatedCabby.head must beEqualTo(updatableCabby)

        val removedCabby = subject.list(passenger.currentPosition).find(_.equals(passenger))
        removedCabby.isEmpty must beTrue;
      }
    }

  }
}
