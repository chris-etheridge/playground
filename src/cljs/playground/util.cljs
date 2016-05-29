(ns playground.util
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

(defn set-url! [url]
  (set! js/window.location url))


(defn el-by-id [id]
  (js/document.getElementById id))
