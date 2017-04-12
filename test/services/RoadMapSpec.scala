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

        val result = subject list position
        result.isEmpty must beFalse
        result.size must beEqualTo(2)
      }

      "two cabbies for each two different positions" in {
        val subject = RoadMapFixture.create
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

  }
}
