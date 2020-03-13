import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.graphqlize.java.GraphQLResolver;
import org.graphqlize.java.GraphQLizeResolver;

import spark.embeddedserver.jetty.HttpRequestWrapper;

import static spark.Spark.*;

import javax.sql.DataSource;
import java.util.Map;

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
    // config.setJdbcUrl("jdbc:postgresql://localhost:5432/sakila");
    // config.setUsername("postgres");
    // config.setPassword("postgres");
    config.setJdbcUrl("jdbc:mysql://localhost:3306/sakila");
    config.setUsername("root");
    config.setPassword("mysql123");
    return new HikariDataSource(config);
  }

  public static void main(String[] args) {
    DataSource dataSource = getDataSource();
    System.out.println("Initializing GraphQLize...");
    GraphQLResolver graphQLResolver = new GraphQLizeResolver(dataSource);
    System.out.println("GraphQLize Initialized");

    staticFiles.location("/public");

    post("/graphql", (req, res) -> {
      Gson gson = new Gson();
      GraphQLRequest graphQlRequest = gson.fromJson(req.body(), GraphQLRequest.class);

      String query = graphQlRequest.getQuery();
      Map<String, Object> variables = graphQlRequest.getVariables();
      String result = graphQLResolver.resolve(query, variables);

      res.header("Content-Type", "application/json");
      return result;
    });
  }
}
