(ns chase.handler
  (:require
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-js include-css]]
   [chase.middleware :refer [wrap-middleware]]
   [environ.core :as env]
   [polaris.core :as polaris]
   [manifold.stream :as stream]
   [aleph.http :as http]))

(def mount-target
  [:div#app
   [:h3 "ClojureScript has not been compiled!"]
   [:p "please run "
    [:b "lein figwheel"]
    " in order to start the compiler"]])

(def loading-page
  (html
   [:html
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     (include-css (if (env/env :dev) "/css/site.css" "/css/site.min.css"))]
    [:body
     mount-target
     (include-js "/js/app.js")]]))

(defn home
  [request]
  {:status 200
   :headers {"content-type" "text/html"}
   :body loading-page})

(defn chase-home
  [request]
  {:status 200
   :headers {"content-type" "text/html"}
   :body (pr-str {:data "Chase!"})})

(defn chase-game
  [request]
  {:status 200
   :headers {"content-type" "text/plain"}
   :body (pr-str {:data {:game (-> request :params :game)}})})

(defn chase-channel
  [request]
  (try
    (let [stream @(http/websocket-connection request)]
      (stream/connect stream stream))
    (catch Exception e (println "BAD WEBSOCKET" e))))

(defn route-definitions
  []
  [["/" :home #'home]
   ["/chase" :chase-home #'chase-home
    [["/:game" :chase-game #'chase-game
      [["/channel" :chase-channel #'chase-channel]]]]]])

(defn make-routes
  []
  (polaris/build-routes (route-definitions)))

(defn make-handler
  []
  (-> (make-routes)
      polaris/router
      wrap-middleware))

