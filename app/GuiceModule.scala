import java.io.FileNotFoundException

import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import services.{DriversMap, DriversRoadMap, PassengersMap, PassengersRoadMap}

import scala.io.Source.fromInputStream

class GuiceModule(environment: Environment, configuration: Configuration) extends AbstractModule {

  private lazy val roads: Array[Array[Boolean]] = {
    val file = environment.resourceAsStream("cidade.txt").map(fromInputStream)
    file.fold(ifEmpty = throw new FileNotFoundException)(file => {
      file.getLines().map(linha =>
        linha.map(_.equals(',')).toArray
      ).toArray
    })
  }

  override def configure() = {
    bind(classOf[DriversMap]).toInstance(DriversRoadMap(roads))
    bind(classOf[PassengersMap]).toInstance(PassengersRoadMap(roads))
  }
}
