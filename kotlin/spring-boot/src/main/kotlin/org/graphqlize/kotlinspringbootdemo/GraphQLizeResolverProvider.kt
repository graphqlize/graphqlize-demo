package org.graphqlize.kotlinspringbootdemo

import org.graphqlize.java.GraphQLizeResolver
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
class GraphQLizeResolverProvider(dataSource : DataSource) {
    val graphQLizeResolver = GraphQLizeResolver(dataSource)
    @Bean
    fun graphQLizeResolver() = graphQLizeResolver
}


