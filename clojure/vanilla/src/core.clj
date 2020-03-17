(ns core
  (:require [com.walmartlabs.lacinia :as lacinia]
            [hikari-cp.core :as hikari]
            [graphqlize.lacinia.core :as l]))

#_(def db-spec (hikari/make-datasource {:adapter           "postgresql"
                                        :database-name     "sakila"
                                        :server-name       "localhost"
                                        :port-number       5432
                                        :maximum-pool-size 1
                                        :username          "postgres"
                                        :password          "postgres"}))

(def db-spec (hikari/make-datasource {:server-name       "localhost"
                                      :maximum-pool-size 1
                                      :jdbc-url          "jdbc:mysql://localhost:3306/sakila"
                                      :driver-class-name "com.mysql.cj.jdbc.MysqlDataSource"
                                      :username          "root"
                                      :password          "mysql123"}))

(def lacinia-schema (l/schema db-spec))

(defn execute
  ([query]
   (lacinia/execute lacinia-schema query nil nil))
  ([query variables]
   (lacinia/execute lacinia-schema query variables nil)))

(comment
  ;; sample queries
  (execute "query { actorByActorId(actorId: 1) { firstName, lastName } }")
  (execute "query($actorId: Int!) { actorByActorId(actorId: $actorId) { firstName, lastName } }"
           {:actorId 1})
  ;; introspection queries
  (execute "{__schema {types {name}}}")
  (execute "{__type(name: \"Actor\") { fields { name type { name kind ofType { name kind }}}}}"))

(defn -main []
  (prn "Hello, World!"))