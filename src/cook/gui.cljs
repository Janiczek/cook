(ns cook.gui)

(defn by-id [id]
  (.getElementById js/document id))

(def el-history (by-id "history"))
(def el-input   (by-id "input"))
