(ns playground.chat.ui.core
  (:require [rum.core :as rum]
            [cljs.core.async :as async]
            [datascript.core :as d]
            [playground.util :as util]
            [playground.events :as events]
            [playground.routes :as routes])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;; local temp state
(def *app-state (atom {:messages    {1 {:msg       "Hello, world!"
                                        :user/name "Chris"
                                        :user/id   5}
                                     2 {:msg       "Good bye, world!"
                                        :user/name "Tim"
                                        :user/id   10}
                                     3 {:msg       "Nice memes."
                                        :user/name "Reggie"
                                        :user/id   15}}
                       :user/name   "Chris Etheridge"
                       :user/active true
                       ;; me is my current ID
                       :user/me     5}))

;;; event bus
;; event bus is an async channel
;; events get dropped on and parsed
(def event-bus (async/chan 0))

(def names ["tom" "timmy" "kitty" "fishboy"])

(defn next-msg-id []
  (-> @*app-state :messages count inc))

(defn me []
  (:user/me @*app-state))

(defn send-msg [chan text]
  (let [payload {:message {:id        (next-msg-id)
                           :user/name (rand-nth names)
                           :user/id   (rand-nth [(me) (range 20)])
                           :msg       text}}]
    (async/put! chan [:msg-send payload])))

(defmulti action! (fn [payload] (first payload)))

(defmethod action! :default [[_ payload]]
  :no-op)

(defmethod action! :msg-send [[_ payload]]
  (swap! *app-state assoc-in [:messages (get-in payload [:message :id])]
         {:user/name (get-in payload [:message :user/name])
          :msg       (get-in payload [:message :msg])
          :user/id   (get-in payload [:message :user/id])}))

;;; components
(rum/defc message [[i {:keys [msg user/name user/id]}] user]
  [:.message__container {:class (when (= user id) "me")}
   [:.row
    [:.text msg]
    [:.user (if (= user id) "me" name)]]])

(rum/defc compose-pane [bus]
  [:#compose
   [:.form-group
    [:textarea.compose__text__area.form-control
     {:placeholder "Reply..."
      :auto-focus  true
      :on-key-down (util/on-textarea-keydown #(send-msg bus %))}]]
   [:.hr]
   [:.btn.btn-primary {:on-click (fn [e]
                                   (routes/go! :index)
                                   (.preventDefault e))} "Test for routing"]])

(rum/defc chat-pane < rum/reactive [msgs-ref user-ref]
  (let [msgs (rum/react msgs-ref)]
    [:#chat-pane
     [:.row
      [:.col-md-12
       (map #(message % (rum/react user-ref)) msgs)]]]))

;; window takes the event bus to put events onto
;; and the cursor it needs to watch
(rum/defc window [event-bus]
  [:#chat-window
   [:.col-md-8.col-md-offset-2
    [:.row
     (chat-pane (rum/cursor *app-state [:messages])
                (rum/cursor *app-state [:user/me]))]
    [:.row (compose-pane event-bus)]]])

;; mounts the main window on the specified element
#_(defn start! [element]
    (events/start-bus! event-bus :events-chan action!)
    (rum/mount (window event-bus)
               element))

(routes/add-route :chat {:ctor  window
                         :data  event-bus
                         :start (fn [data]
                                  (events/start-bus! data :events-chan action!))})
