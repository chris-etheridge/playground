(ns playground.chat.core
  (:require [playground.chat.ui.core :as ui]
            [playground.routes :as routes]
            [playground.chat.ui.pages.index]))

(enable-console-print!)

;; start the ui
(routes/go! :chat)
