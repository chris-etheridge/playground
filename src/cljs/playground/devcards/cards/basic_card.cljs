(ns playground.devcards.cards.basic-card
  (:require [rum.core :as rum]
            [cljs.core.async :as async])
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [devcards.core :as dc :refer [defcard]]))

(def *counter (atom {:counter1 0
                    :counter2 0}))

(def events-chan (async/chan 0))

(defn prn-chan! [name chan]
  (go (loop []
        (when-let [value (async/<! chan)]
          (prn name value))
        (recur))))

(prn-chan! :events-chan events-chan)

(defmulti event (fn [args]
                  (:action args)))

(defmethod event 'increment!
  [args]
  (swap! *counter update-in (:path args) inc))

(defmethod event 'decrement!
  [args]
  (swap! *counter update-in (:path args) dec))

(defn parse-chan-event [chan]
  (go (loop []
        (when-let [args (async/<! chan)]
          (event args))
        (recur))))

(parse-chan-event events-chan)

(rum/defc simple-counter < rum/reactive [ref action path]
  [:div {:on-click (fn [_] (async/put! events-chan {:action action
                                                    :path path}))}
   "Clicks (inc):" (rum/react ref)])

(defcard rum-basic-card-inc
  "Increments the counter"
  (simple-counter (rum/cursor *counter [:counter1])
                      'increment!
                      [:counter1]))

(defcard rum-basic-card-dec
  "Decrements the counter"
  (simple-counter (rum/cursor *counter [:counter2])
                      'decrement!
                      [:counter2]))



