package org.graphqlize.kotlinspringbootdemo

import com.fasterxml.jackson.annotation.JsonCreator
import org.graphqlize.java.GraphQLResolver
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class GraphQLRequest @JsonCreator constructor(
        val query: String,
        val variables: Map<String, Any>?
)

@RestController
class GraphQLController(val graphQLResolver: GraphQLResolver) {

    @PostMapping("/graphql")
    fun handle(@RequestBody graphQLRequest: GraphQLRequest): ResponseEntity<String> {
        val result =
            graphQLResolver.resolve(graphQLRequest.query, graphQLRequest.variables)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(result);
    }
}