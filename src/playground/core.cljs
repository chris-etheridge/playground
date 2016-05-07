(ns playground.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(enable-console-print!)

;; defui creates a truly plain javascript object
(defui HelloWorld
  Object
  (render [this]
          ;; get props (:title) off the object
          (dom/div nil (get (om/props this) :title))))

;; om/factory returns a pure react component from
;; an om component
(def hello (om/factory HelloWorld))

(js/ReactDOM.render
  ;; apply a transformation to our component
  (apply dom/div nil
         (map #(hello {:react-key %
                       :title (str "Hello, " %)})
              (range 3)))
  (gdom/getElement "app"))
