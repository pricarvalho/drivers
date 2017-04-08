package services

import javax.inject.Inject

import play.api.Environment
import play.api.cache.CacheApi

trait MapsService {
  def list: Array[Array[Boolean]]

}

class CachingMapsService @Inject()(cache: CacheApi, environment: Environment) extends MapsService {

  import scala.io.Source._

  override def list: Array[Array[Boolean]] = {
    cache.getOrElse("locations") {
      cache.set("locations", cacheLocations)
      this.list
    }
  }

  private def cacheLocations: Array[Array[Boolean]] = {
    val file = environment.resourceAsStream("cidade.txt").map(fromInputStream(_))
    file.fold(ifEmpty = throw LocationNotFoundException())(file => {
      file.getLines().map(linha => {
        linha.map(_.equals(',')).toArray
      }).toArray
    })
  }
}

case class LocationNotFoundException() extends RuntimeException
