(ns playground.events
  (:require [cljs.core.async :as async])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;;; Event bus

(defn start-bus! [chan name f]
  "Starts the event bus loop. Calls 'f' when a value is put on the chan."
  (go (loop []
        (when-let [val (async/<! chan)]
          (prn name val)
          (f val))
        (recur))))


