(ns server
  (:require [hikari-cp.core :as hikari]
            [honeyeql-postgres.core :as heql-pg]
            [io.pedestal.http :as server]
            [com.walmartlabs.lacinia.pedestal :as lacinia-pedestal]
            [graphqlize.lacinia.core :as l]))

(def ds-opts {:adapter           "postgresql"
              :database-name     "sakila"
              :server-name       "localhost"
              :port-number       5432
              :maximum-pool-size 1
              :username          "postgres"
              :password          "postgres"})

(defn main- []
  (with-open [db-spec (hikari/make-datasource ds-opts)]
    (-> (heql-pg/initialize db-spec)
        l/schema
        (lacinia-pedestal/service-map {:graphiql true
                                       :port     8080})
        server/start)))