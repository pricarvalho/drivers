import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import services.{MapsManager, Maps}

class GuiceModule(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure() = {
    bind(classOf[MapsManager]).to(classOf[Maps])
  }
}
