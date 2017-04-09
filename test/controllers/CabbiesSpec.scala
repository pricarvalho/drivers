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

      "'Created' status result" in new WithApplication {
        val teste = "{\n\t\"currentLocation\" : {\n\t\t\"cabby\": {\n\t\t\t\"id\": 1234 \n\t\t},\n\t\t\"car\": {\n\t\t\t\"tag\": \"APP2017\"\t\n\t\t},\n\t\t\"position\" : {\n\t\t\t\"x\": 0,\n\t    \t\"y\": 0\n\t\t}\n\t}\n}"
        val post = route(app, FakeRequest(POST, "/cabbies").withJsonBody(Json.parse(teste))).get

        status(post) must equalTo(CREATED)
      }

      "'NotAcceptable' status result to invalid json request" in new WithApplication {
        val teste = "{\n\t\t\"cabby\": {\n\t\t\t\"id\": 1234 \n\t\t},\n\t\t\"car\": {\n\t\t\t\"tag\": \"APP2017\"\t\n\t\t},\n\t\t\"position\" : {\n\t\t\t\"x\": 0,\n\t    \t\"y\": 0\n\t\t}\n\t}"
        val post = route(app, FakeRequest(POST, "/cabbies").withJsonBody(Json.parse(teste))).get

        status(post) must equalTo(NOT_ACCEPTABLE)
      }

      "'UnsupportedMediaType' status result to invalid request" in new WithApplication {
        val post = route(app, FakeRequest(POST, "/cabbies")).get

        status(post) must equalTo(UNSUPPORTED_MEDIA_TYPE)
      }
    }
  }
}
