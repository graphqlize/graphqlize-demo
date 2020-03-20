package org.graphqlize

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import com.zaxxer.hikari.HikariDataSource
import com.zaxxer.hikari.HikariConfig
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.routing.post
import org.graphqlize.java.GraphQLizeResolver


data class GraphQLRequest @JsonCreator constructor(
    val query: String,
    val variables: Map<String, Any>?
)

fun getDataSource(): HikariDataSource {
    val config = HikariConfig()
//    config.setJdbcUrl("jdbc:postgresql://localhost:5432/sakila");
//    config.setUsername("postgres");
//    config.setPassword("postgres");
    config.jdbcUrl = "jdbc:mysql://localhost:3306/sakila"
    config.username = "root"
    config.password = "mysql123"
    return HikariDataSource(config)
}

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }
    val dataSource = getDataSource()
    val graphQLResolver = GraphQLizeResolver(dataSource)

    routing {
        post("/graphql") {
            val graphQLRequest = call.receive<GraphQLRequest>()
            val result =
                graphQLResolver.resolve(graphQLRequest.query, graphQLRequest.variables)
            call.respondText(result, ContentType.Application.Json)
        }
        static("/") {
            resource("playground.html")
            resource("voyager.html")
        }
    }
}

