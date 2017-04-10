package fixture

import java.io.FileNotFoundException

import play.api.Environment
import services.RoadMapService

import scala.io.Source.fromInputStream

object RoadMap {

  def create: RoadMapService = RoadMapService(roads)

  val roads: Array[Array[Boolean]] = {
    val file = Environment.simple().resourceAsStream("cidade.txt").map(fromInputStream)
    file.fold(ifEmpty = throw new FileNotFoundException)(file => {
      file.getLines().map(linha =>
        linha.map(_.equals(',')).toArray
      ).toArray
    })
  }

}
