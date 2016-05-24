(ns playground.chat.ui.core
  (:require [rum.core :as rum]
            [cljs.core.async :as async]
            [datascript.core :as d])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;; local temp state
(def *app-state (atom {:messages {1 {:msg "Hello, world!"
                                     :user/name "Chris"}
                                  2 {:msg "Good bye, world!"
                                     :user/name "Tim"}
                                  3 {:msg "Nice memes."
                                     :user/name "Reggie"}}
                       :user/name "Chris Etheridge"
                       :user/active true}))

;;; event bus management
(def event-bus (async/chan 0))

(defn next-msg-id []
  (inc (count (:messages @*app-state))))

(defn send-msg [chan text]
  (let [payload {:message {:id (next-msg-id)
                           :user/name "Chris"
                           :msg text}}]
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
          :msg (get-in payload [:message :msg])}))

(defn parse-chan! [name chan]
  (go (loop []
        (when-let [payload (async/<! chan)]
          (prn name payload)
          (action! payload))
        (recur))))

(parse-chan! :events-chan event-bus)

;;; components

(rum/defc message [[id msg]]
  (let [user (:user/name msg)
        text (:msg msg)]
    [:#message
     [:.text text]
     [:.user user]]))

(defn- textarea-keydown [callback]
  (fn [e]
    (if (and (== (.-keyCode e) 13)
             (not (.-shiftKey e)))
      (do
        (callback (.. e -target -value))
        (set! (.. e -target -value) "")
        (.preventDefault e)))))

(rum/defc compose-pane [bus]
  [:#compose
   [:textarea#compose__text__area
    {:placeholder "Reply..."
     :auto-focus true
     :on-key-down (textarea-keydown #(send-msg bus %))}]])

(rum/defc chat-pane < rum/reactive [ref]
  (let [msgs (rum/react ref)]
    [:#chat-pane
     (map message msgs)]))

;; window takes the event bus to put events onto
;; and the cursor it needs to watch
(rum/defc window [event-bus cursor]
  [:#chat-window
   (chat-pane cursor)
   (compose-pane event-bus)])

(defn start! [element]
  (rum/mount (window event-bus (rum/cursor *app-state [:messages]))
             element))
