package fixture

import java.io.FileNotFoundException

import play.api.Environment
import services.{CabbiesMap, CabbiesRoadMap}

import scala.io.Source.fromInputStream

object RoadMapFixture {

  def create: CabbiesMap = CabbiesRoadMap(roads)

  val roads: Array[Array[Boolean]] = {
    val file = Environment.simple().resourceAsStream("cidade.txt").map(fromInputStream)
    file.fold(ifEmpty = throw new FileNotFoundException)(file => {
      file.getLines().map(linha =>
        linha.map(_.equals(',')).toArray
      ).toArray
    })
  }

}
