(ns playground.devcards.cards.todo
  (:require [rum.core :as rum]
            [playground.util :as util]
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
                       :user/todos {457 {:text       "Take out the trash!"
                                         :completed? false}
                                    458 {:text       "Clean the dog wee."
                                         :completed? true}
                                    459 {:text       "Drink some water."
                                         :completed? false}}}))

(defn toggle-todo [tid done?]
  (swap! *app-state assoc-in [:user/todos tid :completed?] (not done?)))

(defn next-todo-id []
  (-> @*app-state
      :user/todos
      count
      inc))

(defn add-todo [text]
  (let [todo {:text       text
              :completed? false}]
    (swap! *app-state assoc-in [:user/todos (next-todo-id)] todo)))

(rum/defc todo [data]
  [:.row.todo
   [:.col-md-1
    [:input {:type "checkbox"}]]
   [:.col-md-11
    [:p (:text data)]]])


(rum/defc todo-re [[tid todo]]
  [:.row.todo {:class (when (:completed? todo) "complete")}
   [:.col-md-1
    [:input {:type      "checkbox"
             :checked   (:completed? todo)
             :on-change #(toggle-todo tid (:completed? todo))
             :class     (when (:completed? todo)
                          "complete")}]]
   [:.col-md-10
    [:p (:text todo)]]])

(rum/defc todo-list [data]
  ;;(prn (:user/todos @data))
  [:row.todo__cards
   (map #(todo  %) (:user/todos @data))])

(rum/defc todo-list-re < rum/reactive [todos-ref]
  [:row.todo__cards
   (map #(todo-re %) (rum/react todos-ref))])

(rum/defc add-todo-pane []
  [:label "Enter a todo"]
  [:input {:type        "text"
           :on-key-down (util/on-textarea-keydown #(add-todo %))}])

(defcard one-todo
  "A simple todo card"
  (fn [data]
    (todo @data))
  {:id         1
   :text       "Take out the trash."
   :completed? false})

(defcard todo-list
  "List of todos. No interaction with app-state."
  (todo-list *app-state))

(defcard todo-list-reactive
  "Complete 1 todo. Interacting with app-state."
  (fn [state _]
    (todo-list-re (rum/cursor state [:user/todos])))
  *app-state
  {:inspect-data true
   :history      true})

(defcard adding-a-todo
  "Add a todo to app-state"
  (add-todo-pane))

