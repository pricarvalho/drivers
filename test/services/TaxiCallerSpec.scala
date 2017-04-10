package services

import model.{Cabby, Passenger, Position, Route}
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TaxiCallerSpec extends Specification {

  "TaxiCaller" should {

    "do something" in {
      val mapa = new Mapando
      mapa.add(Cabby(tagCar = "APRIL-2017", Position(10,2), statusCode = 1))

      val passenger = Passenger(1, Route(Position(0,0), Position(14,7)))
      val result = TaxiCaller(mapa).callFrom(passenger)

      result.isEmpty must beFalse
    }
  }

  class Mapando extends RoadMap

}
