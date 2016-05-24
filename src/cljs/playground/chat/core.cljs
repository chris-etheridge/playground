(ns playground.chat.core
  (:require [playground.chat.ui.core :as ui]))

(enable-console-print!)

;; start the ui
(ui/start! (js/document.getElementById "app"))
