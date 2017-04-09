package services

import javax.inject.Inject

import model.{CabbyPositionStatus, Position}
import play.api.Environment
import play.api.cache.CacheApi

import scala.collection.mutable.{HashMap, MutableList}

trait MapsManager {

  type CabbiesByPosition = HashMap[Position, MutableList[CabbyPositionStatus]]

  val road: Array[Array[Boolean]]

  def add(position: Position, cabbyStatus: CabbyPositionStatus)

}

class Maps @Inject()(cache: CacheApi, environment: Environment) extends MapsManager {


}

case class LocationNotFoundException() extends RuntimeException
