import org.scalatest.{ Matchers, WordSpec }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._

class WebServerSpec extends WordSpec with Matchers with ScalatestRouteTest {

  val webServer = WebServer
  implicit def rejectionHandler = webServer.rejectionHandler

  "The server" should {
    "return a proper response for hello route" in {
      Get("/hello") ~> webServer.route ~> check {
        responseAs[String] shouldEqual """{"code":200,"message":"Hello!! How are you?"}"""
        status shouldEqual StatusCodes.OK
      }
    }

    "return proper response for check route if all parameters are provided" in {
      Get("/check?color=red&bgColor=white") ~> webServer.route ~> check {
        responseAs[String] shouldEqual """{"code":200,"message":"Your preference is color red with background color white."}"""
      }
    }

    "return a missing query parameter rejection for check route when parameters are not supplied" in {
        Get("/check") ~> webServer.route ~> check {
          rejection shouldEqual MissingQueryParamRejection("color")
        }
    }

    "return a formatted response when a parameter rejection is encountered on check route" in {
      Get("/check?color=red") ~> Route.seal(webServer.route) ~> check {
        responseAs[String] shouldEqual """{"code":400,"type":"Missing Parameter","message":"The required bgColor was not found."}"""
      }
    }

    "return a formatted response when a random route is hit" in {
      Get("/random") ~> Route.seal(webServer.route) ~> check {
        responseAs[String] shouldEqual """{"code":404,"type":"NotFound","message":"The requested resource could not be found."}"""
        status shouldEqual StatusCodes.NotFound
      }
    }

    "return a formatted response when a incorrect method call is hit" in {
      Put("/hello") ~> Route.seal(webServer.route) ~> check {
        responseAs[String] shouldEqual """{"code":405,"type":"Not Allowed","message":"Access to List(GET) is not allowed."}"""
      }
    }
  }
}
