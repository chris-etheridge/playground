(ns playground.devcards.cards.todo
  (:require [rum.core :as rum]
            [cljs.core.async :as async]
            [devcards.core :as dc])
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [devcards.core :refer [defcard]]))


(defcard todo-docs
  "The global state will contain all of the todos that the user currently has.
  Each todo will have the following structure:

  ```clj
  {:id 1
   :text \"Take out the trash\"
   :completed? false}
  ```")


#_(defcard with-opts
    "Card with extra options."
    {}
    {:inspect-data true
     :history      true})

(def *app-state (atom {:user/name  "Chris"
                       :user/todos [{:id         356
                                     :text       "Take out the trash!"
                                     :completed? false}
                                    {:id         357
                                     :text       "Clean the dog wee."
                                     :completed? true}
                                    {:id         359
                                     :text       "Drink some water."
                                     :completed? false}]}))

(rum/defc one-todo-card [data]
  [:div.row
   [:.col-md-6
    [:.input-group
     [:span.input-group-addon
      [:input {:type "checkbox"}]]
     (:text @data)]]])

(defcard one-todo
  "A simple todo card"
  (fn [data]
    (one-todo-card data))
  {:id         1
   :text       "Take out the trash."
   :completed? false})


