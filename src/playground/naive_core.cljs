(ns playground.naive-core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

;;; NOTES
;; this design is naive because the components are tightly
;; coupled to a global atom. this can be very detrimental
;; to performance.

(enable-console-print!)

(def app-state (atom {:count 0}))

(defui Counter
  Object
  (render [this]
          (let [{:keys [count]} (om/props this)]
            (dom/div nil
                     (dom/span nil (str "Count: " count))
                     (dom/button #js {:onClick (fn [e]
                                                 (swap! app-state update-in [:count] inc))}
                                 "Click Me!")))))

;; in om, application state changes are managed by
;; a reconciler
(def reconciler
  (om/reconciler {:state app-state}))

;; om/add-root! takes a reconciler, a component, and
;; and a DOM element
;; om/add-root! does the mounting of the components for us
(om/add-root! reconciler
              Counter
              (gdom/getElement "app"))
