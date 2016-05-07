(ns playground.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(enable-console-print!)

;; defui creates a truly plain javascript object
(defui HelloWorld
  Object
  (render [this]
          (dom/div nil "Hello World")))

(def hello (om/factory HelloWorld))

(js/ReactDOM.render (hello) (gdom/getElement "app"))
