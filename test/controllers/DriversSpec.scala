package controllers

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class DriversSpec extends Specification {

  "Drivers" should {

    "save a driver and return" in {

      "'Created' to valid result" in new WithApplication {
        val json = "{\t\n\t\"tagCar\" : \"APRIL-2017\",\n\t\"currentPosition\" : {\n\t\t\"x\": 10,\n    \t\"y\": 2\n\t}\n}"
        val post = route(app, FakeRequest(POST, "/drivers").withJsonBody(Json.parse(json))).get

        status(post) must equalTo(CREATED)
      }

      "'BadRequest' to invalid json request" in new WithApplication {
        val json = "{\n\t\"currentPosition\" : {\n\t\t\"y\": 0\n\t}\n}"
        val post = route(app, FakeRequest(POST, "/drivers").withJsonBody(Json.parse(json))).get

        status(post) must equalTo(BAD_REQUEST)
      }

      "'UnsupportedMediaType' to invalid request" in new WithApplication {
        val post = route(app, FakeRequest(POST, "/drivers")).get

        status(post) must equalTo(UNSUPPORTED_MEDIA_TYPE)
      }
    }
  }
}
