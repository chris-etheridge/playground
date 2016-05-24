(ns playground.chat.ui.core
  (:require [rum.core :as rum]
            [cljs.core.async :as async]
            [datascript.core :as d]
            [playground.chat.ui.util :as util])
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

;;; event bus management
(def event-bus (async/chan 0))

;; dummy names
(def names ["tom" "timmy" "kitty" "fishboy"])

(defn next-msg-id []
  (inc (count (:messages @*app-state))))

(defn me []
  (:user/me @*app-state))

(defn send-msg [chan text]
  (let [payload {:message {:id        (next-msg-id)
                           :user/name (rand-nth names)
                           :user/id   (me)
                           :msg       text}}]
    (prn payload)
    (async/put! chan [:msg-send payload])))

(defn prn-chan! [name chan]
  (go (loop []
        (when-let [value (async/<! chan)]
          (prn name value))
        (recur))))

(defmulti action! (fn [payload] (first payload)))

(defmethod action! :default [[_ payload]]
  :no-op)

(defmethod action! :msg-send [[_ payload]]
  (swap! *app-state assoc-in [:messages (get-in payload [:message :id])]
         {:user/name (get-in payload [:message :user/name])
          :msg       (get-in payload [:message :msg])
          :user/id   (get-in payload [:message :user/id])}))

(defn parse-chan! [name chan]
  (go (loop []
        (when-let [payload (async/<! chan)]
          (prn name payload)
          (action! payload))
        (recur))))

(parse-chan! :events-chan event-bus)

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
      :on-key-down (util/on-textarea-keydown #(send-msg bus %))}]]])

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
    [:.row(compose-pane event-bus)]]])

;; mounts the main window on the specified element
(defn start! [element]
  (rum/mount (window event-bus)
             element))
