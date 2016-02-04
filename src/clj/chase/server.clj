(ns chase.server
  (:require
   [environ.core :as env]
   [com.stuartsierra.component :as component]
   [aleph.http :as http]
   [chase.handler :as handler])
  (:gen-class))

(defrecord ChaseServer [port]
  component/Lifecycle
  (start [component]
    (let [handler (handler/make-handler)
          server (http/start-server handler {:port port})]
      (assoc
       component
       :handler handler
       :server server)))
  (stop [component]
    (.close (:server component))
    (dissoc component :handler :server)))

(defn new-chase-server
  [config]
  (map->ChaseServer config))

(defn start
  [port]
  (let [server (new-chase-server {:port port})]
    (println "chase!")
    (component/start server)))

(defn -main
  [& args]
  (let [port (Integer/parseInt (or (env/env :port) "12321"))]
    (start port)))

