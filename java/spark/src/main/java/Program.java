import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.graphqlize.java.GraphQLResolver;
import org.graphqlize.java.GraphQLizeResolver;

import javax.sql.DataSource;

import static spark.Spark.*;

class GraphQlRequest {
  private String query;

  public String getQuery() {
    return query;
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
      GraphQlRequest graphQlRequest = gson.fromJson(req.body(), GraphQlRequest.class);
      String query = graphQlRequest.getQuery();
      String result = graphQLResolver.resolve(query);
      res.header("Content-Type", "application/json");
      return result;
    });
  }
}
