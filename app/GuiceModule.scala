import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import services.{MapsService, CachingMapsService}

class GuiceModule(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure() = {
    bind(classOf[MapsService]).to(classOf[CachingMapsService])
  }
}
