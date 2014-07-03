(ns cook.gui.history
  (:require [cook.gui :as g]))

(defn content  []       (aget g/el-history "innerHTML"))
(defn content! [string] (aset g/el-history "innerHTML" string))

(defn scroll-to-bottom! [] (aset g/el-history "scrollTop"
                                 (aget g/el-history "scrollHeight")))

(defn append! [string & {:keys [extra-line] ;; damn CSS :)
                         :or {extra-line false}}]
  (let [current (content)
        appended (str current
                      (if extra-line "\n\n" "\n")
                      string)]
    (content! appended)
    (scroll-to-bottom!)))

(defn append-input! [string]
  (append! (str "<span class=\"command\">&gt; " string "</span>")
           :extra-line true))
