package services

import fixture.MapFixture
import model.{Driver, Passenger, Position, Route}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
@RunWith(classOf[JUnitRunner])
class MapsSpec extends Specification {

  "DriversMap" should {

    "add and list" in {

      "two different drivers in the same positions" in {
        val subject = MapFixture.createDriversRoadMap
        val position = Position(10, 2)
        subject.add(Driver(tagCar = "APRIL-2017", position, status = 1))
        subject.add(Driver(tagCar = "APRIL-2018", position, status = 1))

        val result = subject list position
        result.isEmpty must beFalse
        result.size must beEqualTo(2)
      }

      "two drivers for each two different positions" in {
        val subject = MapFixture.createDriversRoadMap
        val firstPosition = Position(10, 2)
        val secondPosition = Position(2, 2)
        subject.add(Driver(tagCar = "APRIL-2017", firstPosition, status = 1))
        subject.add(Driver(tagCar = "APRIL-2018", secondPosition, status = 1))

        val firstResult = subject list firstPosition
        firstResult.isEmpty must beFalse
        firstResult.size must beEqualTo(1)

        val secondResult = subject list secondPosition
        secondResult.isEmpty must beFalse
        secondResult.size must beEqualTo(1)
      }

    }

    "move" in {

      "one driver to another position" in {
        val subject = MapFixture.createDriversRoadMap

        val driver = Driver(tagCar = "APRIL-2017", Position(10, 2), status = 1)
        subject.add(driver)
        val addDriver = subject.list(driver.currentPosition).find(_.equals(driver))
        addDriver.head must beEqualTo(driver)

        val updatableDriver = addDriver.head.copy(currentPosition = Position(12, 2))
        subject.movePosition(addDriver.head, updatableDriver.currentPosition)
        val updatedDriver = subject.list(updatableDriver.currentPosition).find(_.equals(driver))
        updatedDriver.isEmpty must beFalse
        updatedDriver.head must beEqualTo(updatableDriver)

        val removedDriver = subject.list(driver.currentPosition).find(_.equals(driver))
        removedDriver.isEmpty must beTrue;
      }
    }

  }

  "PassengersMap" should {

    "add and list" in {

      "two different drivers in the same positions" in {
        val subject = MapFixture.createPassengersMap
        val position = Position(10, 2)
        subject.add(Passenger(Route(position,Position(12,2))))
        subject.add(Passenger(Route(position,Position(12,2))))

        val result = subject list position
        result.isEmpty must beFalse
        result.size must beEqualTo(2)
      }

      "two drivers for each two different positions" in {
        val subject = MapFixture.createPassengersMap
        val firstPosition = Position(10, 2)
        val secondPosition = Position(2, 2)
        subject.add(Passenger(Route(firstPosition,Position(12,2))))
        subject.add(Passenger(Route(secondPosition,Position(12,2))))

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

        val passenger = Passenger(Route(Position(0,0),Position(12,2)))
        subject.add(passenger)
        val addDriver = subject.list(passenger.currentPosition).find(_.equals(passenger))
        addDriver.head must beEqualTo(passenger)

        val updatableDriver = addDriver.head.copy(currentPosition = Position(2, 0))
        subject.movePosition(addDriver.head, updatableDriver.currentPosition)
        val updatedDriver = subject.list(updatableDriver.currentPosition).find(_.equals(passenger))
        updatedDriver.isEmpty must beFalse
        updatedDriver.head must beEqualTo(updatableDriver)

        val removedDriver = subject.list(passenger.currentPosition).find(_.equals(passenger))
        removedDriver.isEmpty must beTrue;
      }
    }

  }
}
