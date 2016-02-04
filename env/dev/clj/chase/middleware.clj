(ns chase.middleware
  (:require [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.file-info :refer [wrap-file-info]]))

(defn wrap-middleware [handler]
  (-> handler
      (wrap-defaults site-defaults)
      (wrap-file "resources")
      wrap-file-info
      wrap-exceptions
      wrap-reload))
