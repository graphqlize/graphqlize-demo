import org.graphqlize.scala._
import org.scalatra._
import javax.servlet.ServletContext
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.graphqlize.java.GraphQLizeResolver

class ScalatraBootstrap extends LifeCycle {
  def dataSource: HikariDataSource = {
    val config = new HikariConfig
     config.setJdbcUrl("jdbc:postgresql://localhost:5432/sakila")
     config.setUsername("postgres")
     config.setPassword("postgres")
//    config.setJdbcUrl("jdbc:mysql://localhost:3306/sakila")
//    config.setUsername("root")
//    config.setPassword("mysql123")
    new HikariDataSource(config)
  }

  override def init(context: ServletContext) {
    val graphqlResolver = new GraphQLizeResolver(dataSource)
    context.mount(new GraphQLizeScalatraServlet(graphqlResolver), "/*")
  }
}
