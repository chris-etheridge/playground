(ns playground.chat.core
  (:require [playground.chat.ui.core :as ui]
            [playground.chat.ui.routes :as routes]
            [playground.chat.ui.pages.index :as index]
            [playground.chat.ui.events :as events]))

(enable-console-print!)

;; start the ui
(routes/go! :chat)
