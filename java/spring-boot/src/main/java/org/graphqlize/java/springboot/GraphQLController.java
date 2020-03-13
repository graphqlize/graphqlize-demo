package org.graphqlize.java.springboot;

import org.graphqlize.java.GraphQLResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

class GraphQLRequest {
  private String query;

  String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }
}

@RestController
public class GraphQLController {
  private final GraphQLResolver graphQLResolver;

  public GraphQLController(GraphQLResolver graphQLResolver) {
    this.graphQLResolver = graphQLResolver;
  }

  @PostMapping("/graphql")
  public ResponseEntity handle(@RequestBody GraphQLRequest graphQLRequest) {
    String result = graphQLResolver.resolve(graphQLRequest.getQuery());
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, "application/json")
            .body(result);
  }
}
