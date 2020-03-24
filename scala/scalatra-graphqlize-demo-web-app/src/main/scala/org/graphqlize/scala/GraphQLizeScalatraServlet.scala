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
    val gqlReq = JsonMethodsExt.parse(compact(render(parsedBody))).extract[GraphQLRequest]
    if (gqlReq.variables.isDefined)
      graphQLResolver.resolve(gqlReq.query, gqlReq.variables.get.asJava)
    else
      graphQLResolver.resolve(gqlReq.query)
  }
}

object JsonMethodsExt extends org.json4s.jackson.JsonMethods {
  mapper.disable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)
  def parse(in: JsonInput): JValue = super.parse(in, useBigIntForLong = false)
  def parseOpt(in: JsonInput): Option[JValue] = super.parseOpt(in, useBigIntForLong = false)
}
