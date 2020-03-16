(ns server
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.request :as request]
            [ring.middleware.resource :as resource]
            [ring.middleware.content-type :as content-type]
            [ring.middleware.not-modified :as not-modified]
            [reitit.ring :as ring]

            [com.walmartlabs.lacinia :as lacinia]
            [clojure.data.json :as json]
            [hikari-cp.core :as hikari]
            [graphqlize.lacinia.core :as l]))

(def db-spec (hikari/make-datasource {:adapter           "postgresql"
                                      :database-name     "sakila"
                                      :server-name       "localhost"
                                      :port-number       5432
                                      :maximum-pool-size 1
                                      :username          "postgres"
                                      :password          "postgres"}))

(def lacinia-schema (l/schema db-spec))

(defn handler [request]
  (let [{:keys [query variables]} (json/read-str (request/body-string request) :key-fn keyword)
        result                    (lacinia/execute lacinia-schema query variables nil)]
    {:status  200
     :body    (json/write-str result)
     :headers {"Content-Type" "application/json"}}))

(def app
  (-> (ring/ring-handler (ring/router ["/graphql" {:post handler}]))
      (resource/wrap-resource "static")
      content-type/wrap-content-type
      not-modified/wrap-not-modified))

(defn start-server []
  (jetty/run-jetty app {:join? false
                        :port  8080}))

(.addShutdownHook (Runtime/getRuntime)
                  (Thread. (fn []
                             (.close db-spec))))

(defn -main []
  (start-server))

(comment
  (def server (start-server))
  (.stop server))