package controllers

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class CabbiesSpec extends Specification {

  "Cabbies" should {

    "save a cabby and return" in {

      "'Created' to valid result" in new WithApplication {
        val json = "{\t\"tagCar\" : \"APRIL-2017\",\n\t\"currentPosition\" : {\n\t\t\"x\": 0,\n    \t\"y\": 0\n\t}\n}"
        val post = route(app, FakeRequest(POST, "/cabbies").withJsonBody(Json.parse(json))).get

        status(post) must equalTo(CREATED)
      }

      "'NotAcceptable' to invalid json request" in new WithApplication {
        val json = "{\n\t\"currentPosition\" : {\n\t\t\"y\": 0\n\t}\n}"
        val post = route(app, FakeRequest(POST, "/cabbies").withJsonBody(Json.parse(json))).get

        status(post) must equalTo(NOT_ACCEPTABLE)
      }

      "'UnsupportedMediaType' to invalid request" in new WithApplication {
        val post = route(app, FakeRequest(POST, "/cabbies")).get

        status(post) must equalTo(UNSUPPORTED_MEDIA_TYPE)
      }
    }
  }
}
