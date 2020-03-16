(ns server-with-coercion
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.resource :as resource]
            [ring.middleware.content-type :as content-type]
            [ring.middleware.not-modified :as not-modified]
   
            [reitit.ring :as ring]
            [reitit.ring.coercion :as rrc]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [reitit.coercion.spec :as reitit-spec]

            [com.walmartlabs.lacinia :as lacinia]
            [hikari-cp.core :as hikari]
            [graphqlize.lacinia.core :as l]
            [clojure.spec.alpha :as s]))


(s/def ::query string?)
(s/def ::variables map?)
(s/def ::graphql-request (s/keys :req-un [::query]
                                 :opt-un [::variables]))

(s/def ::data map?)
(s/def ::graphql-response (s/keys :out-un [::data]))

(def db-spec (hikari/make-datasource {:adapter           "postgresql"
                                      :database-name     "sakila"
                                      :server-name       "localhost"
                                      :port-number       5432
                                      :maximum-pool-size 1
                                      :username          "postgres"
                                      :password          "postgres"}))

(def lacinia-schema (l/schema db-spec))

(defn handler [request]
  (let [{:keys [query variables]}
        (get-in request [:parameters :body])]
    {:status  200
     :body    (lacinia/execute lacinia-schema query variables nil)}))

(def router
  (ring/router
   ["/graphql" {:post     {:handler    handler 
                           :coercion   reitit-spec/coercion
                           :responses {200 {:body ::graphql-response}}
                           :parameters  {:body ::graphql-request}}}]
   {:data {:muuntaja m/instance
           :middleware [muuntaja/format-middleware
                        rrc/coerce-request-middleware
                        rrc/coerce-response-middleware]}}))

(def app
  (-> (ring/ring-handler router)
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