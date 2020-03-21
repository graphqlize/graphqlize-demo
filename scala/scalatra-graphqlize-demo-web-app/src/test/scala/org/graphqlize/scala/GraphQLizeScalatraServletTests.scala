package org.graphqlize.scala

import org.scalatra.test.scalatest._

class GraphQLizeScalatraServletTests extends ScalatraFunSuite {

  addServlet(classOf[GraphQLizeScalatraServlet], "/*")

  test("GET / on GraphQLizeScalatraServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
