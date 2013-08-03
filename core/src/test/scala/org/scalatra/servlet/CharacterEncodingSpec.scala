package org.scalatra.servlet

import org.scalatra.ScalatraServlet
import org.scalatra.test.specs2.MutableScalatraSpec

class CharacterEncodingSpecServlet extends ScalatraServlet {
  get("/respect-character-encoding-when-serving-byte-arrays/:encoding") {
    if (params("encoding") != "use-default") {
      response.setCharacterEncoding(params("encoding"))
    }
    contentType = "text/plain"
    // Return Array[Byte], because we test the Array[Byte] case in ScalatraBase#renderPipeline
    "here are the bytes".getBytes
  }
}

class CharacterEncodingSpec extends MutableScalatraSpec {
  mount(new CharacterEncodingSpecServlet, "/*")

  "When the action returns an Array[Byte] with content type text/subtype, the response" should {
    "respect the #setCharacterEncoding invocation" in {
      get("/respect-character-encoding-when-serving-byte-arrays/Cp278") {
        response.getHeader("Content-Type") must equalTo("text/plain;charset=Cp278")
      }
    }

    "use the default character encoding if the action does not call #setCharacterEncoding" in {
      get("/respect-character-encoding-when-serving-byte-arrays/use-default") {
        response.getHeader("Content-Type") must equalTo("text/plain;charset=UTF-8")
      }
    }
  }
}
