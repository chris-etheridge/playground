(ns playground.chat.ui.routes
  (:require [rum.core :as rum]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Routes


;;; A route takes the following structure
;;; {:name :keyword-to-refer-route
;;;  :ctor rum.root.component.fn
;;;  :start fn.start.start}

(defonce *routes (atom {}))


;; adds a route to the routing table
(defn add-route [key opts]
  (swap! *routes assoc key opts))

;; go to a route
(defn go! [route-kw debug?]
  (when debug
    (prn "going: " route-kw)
    (prn "routes table: " @*routes)
    (prn "route: " route)
    (prn "ctor: " (:ctor route))
    (prn "data: " (:data route)))
  (when-let [route (route-kw @*routes)]
    (rum/mount ((:ctor route) (:data route)) (js/document.getElementById "app"))
    (when-let [startfn (:start route)]
      ((:start route) (:data route)))))
