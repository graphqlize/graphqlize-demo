package org.graphqlize.scala

import com.fasterxml.jackson.databind.DeserializationFeature
import org.graphqlize.java.GraphQLResolver
import org.scalatra._
import org.json4s._
import org.scalatra.json._

import scala.collection.JavaConverters._

case class GraphQLRequest(query : String, variables : Option[Map[String, Object]])

class GraphQLizeScalatraServlet(graphQLResolver : GraphQLResolver) extends ScalatraServlet with JacksonJsonSupport {
  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  post("/graphql") {
    println(request.body)
    val gqlReq = parse(request.body, useBigIntForLong = false).extract[GraphQLRequest]
    gqlReq.variables.get.foreach {
      case (name, value) => println(s"$name = $value (${value.getClass})")
    }
    if (gqlReq.variables.isDefined)
      graphQLResolver.resolve(gqlReq.query, gqlReq.variables.get.asJava)
    else
      graphQLResolver.resolve(gqlReq.query)
  }



}

object JsonMethodsExt extends org.json4s.jackson.JsonMethods {
  // we need this because otherwise parse(useBigIntForLong = false) doesn't work.
  // It should be patched in future json4s versions
  mapper.disable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)
}
