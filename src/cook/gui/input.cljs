(ns cook.gui.input
  (:require [cook.gui :as g]))

(defn content [] (aget g/el-input "value"))
(defn clear!  [] (aset g/el-input "value" ""))

(defn focus! [] (.focus g/el-input))
