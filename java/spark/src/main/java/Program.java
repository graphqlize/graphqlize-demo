import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.graphqlize.java.GraphQLResolver;
import org.graphqlize.java.GraphQLizeResolver;
import spark.ResponseTransformer;

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

class JsonTransformer implements ResponseTransformer {

  private Gson gson = new Gson();

  @Override
  public String render(Object model) {
    return gson.toJson(model);
  }

}

public class Program {

  private static DataSource getDataSource() {
    HikariConfig config = new HikariConfig();
//    config.setJdbcUrl("jdbc:postgresql://localhost:5432/sakila");
//    config.setUsername("postgres");
//    config.setPassword("postgres");
    config.setJdbcUrl("jdbc:mysql://localhost:3306/sakila");
    config.setUsername("root");
    config.setPassword("mysql123");
    return new HikariDataSource(config);
  }

  public static void main(String[] args) {
    DataSource dataSource = getDataSource();
    GraphQLResolver graphQLResolver = new GraphQLizeResolver(dataSource);

    Gson gson = new Gson();

    staticFiles.location("/public");
    post("/graphql", (req, res) -> {
      GraphQlRequest graphQlRequest = gson.fromJson(req.body(), GraphQlRequest.class);
      return gson.fromJson(graphQLResolver.resolve(graphQlRequest.getQuery()), Object.class);
    }, new JsonTransformer());
  }
}
