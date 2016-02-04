(ns chase.model
  (:require
   [clojure.java.jdbc :as j]))

(def db
  {:subprotocol "postgresql"
   :subname "//127.0.0.1:5432/chase"
   :user (System/getenv "CHASE_USER")
   :password (System/getenv "CHASE_PASSWORD")})

(defn games
  []
  (j/query db ["select * from game"]))

(defn player-games
  [player-id]
  (j/query db ["select * from game where player_id = ?" player-id]))

(defn moves
  [game-id]
  (let [moves (j/query db ["select * from move where game_id = ?" game-id])]
    moves))
