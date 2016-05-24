(ns playground.chat.db.core
  (:require [datascript.core :as d]
            [cljs.core.async :as async]))

;; schema
(def schema {;; message schema
              :message/text {}
              :message/timestamp {}
              :message/unread {}
              ;; user schema
              :user/name {}
              :user/avatar {}
              :user/me {}
              :user/state {}})

(defn setup-db! []
  (d/create-conn schema))




