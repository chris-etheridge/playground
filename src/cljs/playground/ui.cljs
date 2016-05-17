(ns playground.ui
  (:require [clojure.string :as str]
            [cljs.core.async :as async]
            [datascript.core :as d]
            [datascript-chat.util :as u]
            [rum.core :as rum]
            [goog.string]
            [goog.string.format]))



;; mounting

(defn mount [conn bus]
  (rum/mount (window conn event-bus) js/document.body))
