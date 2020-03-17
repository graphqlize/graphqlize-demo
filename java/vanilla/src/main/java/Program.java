import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.graphqlize.java.GraphQLResolver;
import org.graphqlize.java.GraphQLizeResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

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

    String result = graphQLResolver.resolve("query { actorByActorId(actorId: 1) { firstName, lastName } }");
    System.out.println(result);

    Map<String, Object> variables = new HashMap<>();
    variables.put("actorId", 1);
    result = graphQLResolver.resolve(
        "query($actorId: Int!) { actorByActorId(actorId: $actorId) { firstName, lastName } }",
        variables
    );
    System.out.println(result);

    result = graphQLResolver.resolve("{__schema {types {name}}}");
    System.out.println(result);

    result = graphQLResolver.resolve("{__type(name: \"Actor\") { fields { name type { name kind ofType { name kind }}}}}");
    System.out.println(result);
  }
}
