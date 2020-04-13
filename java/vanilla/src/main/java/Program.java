import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.graphqlize.java.GraphQLResolver;
import org.graphqlize.java.GraphQLizeResolver;

public class Program {

  /**
   * For more documentation see configuration:
   *
   * https://github.com/brettwooldridge/HikariCP#initialization
   * https://github.com/brettwooldridge/HikariCP#popular-datasource-class-names
   */
  private static DataSource getDataSource(String configPath) {
    HikariConfig config = new HikariConfig(configPath);
    return new HikariDataSource(config);
  }

  public static void main(String[] args) {
    String path = getEnvOrDefault("GRAPHQLIZE_HIKARI_CONFIG", "config/database.properties");
    Path fullPath = Paths.get(path).toAbsolutePath();
    System.out.println("Using datasource configuration from: " + fullPath);
    DataSource dataSource = getDataSource(fullPath.toString());
    System.out.println("Initializing GraphQLize...");
    GraphQLResolver graphQLResolver = new GraphQLizeResolver(dataSource);
    System.out.println("GraphQLize Initialized");

    String result = graphQLResolver
        .resolve("query { actorByActorId(actorId: 1) { firstName, lastName } }");
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

    result = graphQLResolver.resolve(
        "{__type(name: \"Actor\") { fields { name type { name kind ofType { name kind }}}}}");
    System.out.println(result);
  }

  private static String getEnvOrDefault(String variableName, String defaultValue) {
    String path = System.getenv(variableName);
    if (path == null) {
      return defaultValue;
    }
    return path;
  }
}
