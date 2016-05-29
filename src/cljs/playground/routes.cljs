(ns playground.routes)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Routes

;;; A route takes the following structure
;;; :kw-name {:data data.for.start.fn
;;;           :ctor rum.root.component.fn
;;;           :start fn.to.start}

(defonce *routes (atom {}))

;; adds a route to the routing table
(defn add-route [key opts]
  (swap! *routes assoc key opts))

;; go to a route
(defn go! [route-kw]
  (prn @*routes)
  (when-let [route (route-kw @*routes)]
    (rum/mount ((:ctor route) (:data route nil)) (js/document.getElementById "app"))
    (when-let [startfn (:start route)]
      ((:start route) (:data route)))))
