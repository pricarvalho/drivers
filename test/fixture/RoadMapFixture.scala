package fixture

import java.io.FileNotFoundException

import play.api.Environment
import services.RoadMap

import scala.io.Source.fromInputStream

object RoadMapFixture {

  def create: RoadMap = RoadMap(roads)

  val roads: Array[Array[Boolean]] = {
    val file = Environment.simple().resourceAsStream("cidade.txt").map(fromInputStream)
    file.fold(ifEmpty = throw new FileNotFoundException)(file => {
      file.getLines().map(linha =>
        linha.map(_.equals(',')).toArray
      ).toArray
    })
  }

}
