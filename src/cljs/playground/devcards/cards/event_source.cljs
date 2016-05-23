(ns playground.devcards.cards.event-source
  (:require [rum.core :as rum]
            [cljs.core.async :as async])
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [devcards.core :as dc :refer [defcard]]))

(defcard event-source
  "Cards for event sourcing")
