import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.graphqlize.java.GraphQLResolver;
import org.graphqlize.java.GraphQLizeResolver;

import javax.sql.DataSource;
import java.util.Map;

import static spark.Spark.post;
import static spark.Spark.staticFiles;

class GraphQLRequest {
  private String query;
  private Map<String, Object> variables;

  public String getQuery() {
    return query;
  }

  public Map<String, Object> getVariables() {
    return variables;
  }

  public void setVariables(Map<String, Object> variables) {
    this.variables = variables;
  }

  public void setQuery(String query) {
    this.query = query;
  }
}

public class Program {

  private static DataSource getDataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:postgresql://localhost:5432/sakila");
    config.setUsername("postgres");
    config.setPassword("postgres");
    //config.setJdbcUrl("jdbc:mysql://localhost:3306/sakila");
    //config.setUsername("root");
    //config.setPassword("mysql123");
    return new HikariDataSource(config);
  }

  public static void main(String[] args) {
    DataSource dataSource = getDataSource();
    System.out.println("Initializing GraphQLize...");
    GraphQLResolver graphQLResolver = new GraphQLizeResolver(dataSource);
    System.out.println("GraphQLize Initialized");

    staticFiles.location("/public");

    post("/graphql", (req, res) -> {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      GraphQLRequest graphQlRequest = objectMapper.readValue(req.body(), GraphQLRequest.class);

      String query = graphQlRequest.getQuery();
      Map<String, Object> variables = graphQlRequest.getVariables();
      String result = graphQLResolver.resolve(query, variables);

      res.header("Content-Type", "application/json");
      return result;
    });
  }
}
