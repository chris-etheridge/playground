(ns playground.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(def *app-state (atom {:app/title "Animals"
                       :animals/list [[1 "Ant"] [2 "Antelope"] [3 "Bird"] [4 "Cat"]
                                      [5 "Dog"] [6 "Lion"] [7 "Mouse"] [8 "Monkey"]
                                      [9 "Snake"] [10 "Zebra"]]}))

;; multimethod for our read functions
(defmulti read (fn [env key params] key))

;; default read method
(defmethod read :default
  [{:keys [state] :as env} key params]
  (let [st @state]
    (if-let [[_ value] (find st key)]
      {:value value}
      {:value :not-found})))

;; read the animal list method
(defmethod read :animals/list
  [{:keys [state] :as env} key {:keys [start end]}]
  ;; subvec returns a new vector between the start and end
  ;; index, of another vector
  {:value (subvec (:animals/list @state) start end)})

(defui AnimalsList
  ;; this passes in params into our query
  ;; we pass in 0 and 10 as the start / end params
  static om/IQueryParams
  (params [this]
          ;; start / end controls how many animals to show
          {:start 0 :end 10})
  static om/IQuery
  (query [this]
         ;; we define what we want to query
         ;; we will provide start/end so we parameterize the query
         ;; by using '?'.
         ;; the reconciler will then call the read method, and that read
         ;; method will be the :animals/list one
         '[:app/title (:animals/list {:start ?start :end ?end})])
  Object
  (render [this]
          (let [{:keys [app/title animals/list]} (om/props this)]
            (dom/div nil
                     (dom/h2 nil title)
                     (apply dom/ul nil
                           (map (fn [[i name]]
                                  (dom/li nil (str i "." name)))
                                list))))))

(def reconciler (om/reconciler {:state *app-state
                                :parser (om/parser {:read read})}))

(om/add-root! reconciler
              AnimalsList
              (gdom/getElement "app"))
