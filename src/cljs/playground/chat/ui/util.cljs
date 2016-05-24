(ns playground.chat.ui.util)

(defn on-textarea-keydown [callback]
  (fn [e]
    (if (and (== (.-keyCode e) 13)
             (not (.-shiftKey e)))
      (do
        (callback (.. e -target -value))
        (set! (.. e -target -value) "")
        (.preventDefault e)))))
