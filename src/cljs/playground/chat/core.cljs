(ns playground.chat.core
  (:require [playground.chat.ui.core :as ui]
            [playground.chat.ui.routes :as routes]))

(enable-console-print!)

;; start the ui

(prn "Going!")

(routes/go! :chat)
