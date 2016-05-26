(ns playground.chat.ui.pages.index
  (:require [rum.core :as rum]
            [playground.chat.ui.routes :as routes]))

;;; Index page

(rum/defc root []
  [:h1 "This is the root page"]
  [:.btn.btn-primary {:on-click (fn [e]
                                  (routes/go! :chat)
                                  (.preventDefault e))}])

(routes/add-route :index {:ctor root
                          :data nil})


