(ns playground.todo.core
  (:require [rum.core :as rum]
            [datascript.core :as d]
            [playground.util :as util]))

(rum/defc a-test-comp []
  [:h1 "A test comp goes here!!!"])

(rum/mount (a-test-comp) (util/el-by-id "app"))
