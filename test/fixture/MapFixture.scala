package fixture

import java.io.FileNotFoundException

import play.api.Environment
import services.{DriversMap, DriversRoadMap, PassengersMap, PassengersRoadMap}

import scala.io.Source.fromInputStream

object MapFixture {

  def createDriversRoadMap: DriversMap = DriversRoadMap(roads)
  def createPassengersMap: PassengersMap = PassengersRoadMap(roads)

  val roads: Array[Array[Boolean]] = {
    val file = Environment.simple().resourceAsStream("cidade.txt").map(fromInputStream)
    file.fold(ifEmpty = throw new FileNotFoundException)(file => {
      file.getLines().map(linha =>
        linha.map(_.equals(',')).toArray
      ).toArray
    })
  }

}
