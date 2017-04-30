package controllers

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class PassengersSpec extends Specification {

  "Passengers" should {

    "save a passenger and return" in {

      "'Created' to valid result" in new WithApplication {
        val json = "{\n\t\"originPosition\" : {\n\t\t\"x\": 0,\n    \t\"y\": 0\n\t},\n\t\"targetPosition\" : {\n\t\t\"x\": 14,\n    \t\"y\": 7\n\t}\n\t\n}"
        val post = route(app, FakeRequest(POST, "/passengers").withJsonBody(Json.parse(json))).get

        status(post) must equalTo(CREATED)
      }

      "'BadRequest' to invalid json request" in new WithApplication {
        val json = "{\n\t\"originPosition\" : {\n\t\t\"x\": 0,\n    \t\"y\": 0\n\t}\n}"
        val post = route(app, FakeRequest(POST, "/passengers").withJsonBody(Json.parse(json))).get

        status(post) must equalTo(BAD_REQUEST)
      }

      "'UnsupportedMediaType' to invalid request" in new WithApplication {
        val post = route(app, FakeRequest(POST, "/passengers")).get

        status(post) must equalTo(UNSUPPORTED_MEDIA_TYPE)
      }
    }
  }
}
