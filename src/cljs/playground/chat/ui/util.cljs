(ns playground.chat.ui.util
  (:require [cognitect.transit :as t]))

;;; UI keys util

(defn on-textarea-keydown [callback]
  (fn [e]
    (if (and (== (.-keyCode e) 13)
             (not (.-shiftKey e)))
      (do
        (callback (.. e -target -value))
        (set! (.. e -target -value) "")
        (.preventDefault e)))))

;;; Local storage util

(defn from-local-storage [key]
  "Returns an item from storage"
  (.getItem (.-localStorage js/window) key))

(defn to-local-storage [key val]
  "Puts an item into local storage."
  (.setItem (.-localStorage js/window) key val))

(defn remove-local-storage [key]
  "Remove an item from local storage"
  (.removeItem (.-localStorage js/window) key))

#_(defn dump-state! [state key]
    "Dumps the user's state to the browser local storage. Formats the data as a transit string"
    (let [t (t/writer :json)]
      (to-local-storage (t/write t state) key)))

;;; Browser history util

(defn set-url! [url]
  (set! js/window.location url))

