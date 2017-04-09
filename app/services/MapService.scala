package services

import javax.inject.Inject

import json.CabbySavesRequest
import model.{CabbyPositionStatus, Position}
import play.api.Environment
import play.api.cache.CacheApi

import scala.collection.mutable.{HashMap, MutableList}
import scala.io.Source._
import scala.util.{Failure, Success, Try}

trait MapsManager {

  type CabbiesByPosition = HashMap[Position, MutableList[CabbyPositionStatus]]

  val road: Array[Array[Boolean]]

  def add(cabbySavesRequest: CabbySavesRequest)

}

class Maps @Inject()(cache: CacheApi, environment: Environment) extends MapsManager {

  override val road: Array[Array[Boolean]] = map
  val cabbies: CabbiesByPosition = new CabbiesByPosition

  def unblocked(position: Position) = Try(road(position.x)(position.y)).getOrElse(false)

  override def add(cabby: CabbySavesRequest): Unit = if(unblocked(cabby.currentPosition)) {
    val cabbyUnappropriated = cabby.toUnappropriated
    Try(cabbies.get(cabby.currentPosition)) match {
      case Success(cabbiesOption) => cabbiesOption.foreach(_ += cabbyUnappropriated)
      case Failure(error) => {
        cabbies.put(cabby.currentPosition, new MutableList[CabbyPositionStatus]() += cabbyUnappropriated)
      }
    }
  }

  private def map: Array[Array[Boolean]] = {
    val file = environment.resourceAsStream("cidade.txt").map(fromInputStream(_))
    file.fold(ifEmpty = throw LocationNotFoundException())(file => {
      file.getLines().map(linha => {
        linha.map(_.equals(',')).toArray
      }).toArray
    })
  }
}

case class LocationNotFoundException() extends RuntimeException
