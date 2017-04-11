package services

import fixture.RoadMapFixture
import model.{Cabby, Passenger, Position, Route}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

import scala.collection.mutable.ListBuffer
@RunWith(classOf[JUnitRunner])
class RoadMapSpec extends Specification {

  "RoadMap" should {

    "add" in {

      "two different cabbies in the same positions" in {
        val subject = RoadMapFixture.create
        val position = Position(10, 2)
        subject.add(Cabby(tagCar = "APRIL-2017", position, statusCode = 1))
        subject.add(Cabby(tagCar = "APRIL-2018", position, statusCode = 1))

        val result = subject.listByPosition[Cabby](position)
        result.isEmpty must beFalse
        result.size must beEqualTo(2)
      }

      "two cabbies for each two different positions" in {
        val subject = RoadMapFixture.create
        val firstPosition = Position(10, 2)
        val secondPosition = Position(2, 2)
        subject.add(Cabby(tagCar = "APRIL-2017", firstPosition, statusCode = 1))
        subject.add(Cabby(tagCar = "APRIL-2018", secondPosition, statusCode = 1))

        val firstResult = subject.listByPosition[Cabby](firstPosition)
        firstResult.isEmpty must beFalse
        firstResult.size must beEqualTo(1)

        val secondResult = subject.listByPosition[Cabby](secondPosition)
        secondResult.isEmpty must beFalse
        secondResult.size must beEqualTo(1)
      }

      "two different people in the same positions" in {
        val subject = RoadMapFixture.create
        val position = Position(0, 0)

        val cabby = Cabby(tagCar = "APRIL-2017", position, statusCode = 1)
        subject.add(cabby)
        val passenger = Passenger(1, Route(position, Position(14,7)))
        subject.add(passenger)

        val resultForCabbies: ListBuffer[Cabby] = subject.listByPosition[Cabby](position)
        resultForCabbies.isEmpty must beFalse
        resultForCabbies.size must beEqualTo(1)
        resultForCabbies.contains(cabby) must beTrue
        resultForCabbies.contains(passenger) must beFalse

        val resultForPassengers: ListBuffer[Passenger] = subject.listByPosition[Passenger](position)
        resultForPassengers.isEmpty must beFalse
        resultForPassengers.size must beEqualTo(1)
        resultForPassengers.contains(passenger) must beTrue
        resultForPassengers.contains(cabby) must beFalse
      }
    }

  }
}
