(ns playground.chat.ui.events
  (:require [cljs.core.async :as async])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn start-bus! [chan name f]
  "Starts the event bus loop. Calls 'f' when a value is put on the chan."
  (go (loop []
        (when-let [val (async/<! chan)]
          (if debug
            (do
              (prn name val)
              (f val))
            (f val)))
        (recur))))


