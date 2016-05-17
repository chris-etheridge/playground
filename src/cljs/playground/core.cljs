(ns playground.core
  (:require [rum.core :as rum]
            [datascript.core :as d]
            [cljs.core.async :as async]))

(enable-console-print!)

;; schema

(def schema {;; room schema
              :room/title {}
              ;; message schema
              :message/room {:db/valueType :db.type/ref}
              :message/text {}
              :message/author {:db/valueType :db.type/ref}
              :message/timestamp {}
              :message/unread {}
              ;; user schema
              :user/name {}
              :user/avatar {}
              :user/me {}
              :user/state {}})

(def conn (d/create-conn schema))




