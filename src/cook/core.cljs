(ns cook.core
  (:require [cook.gui :refer [el-input]]
            [cook.gui.history :as h]
            [cook.gui.input   :as i]
            [cook.data.msgs :refer [intro]]
            [cook.state :refer [state
                                new-state]]
            [cook.commands :refer [do-accordingly]]))

(defn handle-command [command]
  (h/append-input! command)
  (h/append! (do-accordingly command))
  (i/clear!))

(defn enter-handler [e]
  (let [enter 13
        key (or (.-which e) (.-keyCode e))]
    (when (= enter key)
      (let [content (i/content)]
        (handle-command content)))))

(defn init []
  (enable-console-print!)
  (reset! state new-state)
  (aset el-input "onkeyup" enter-handler)
  (h/content! intro)
  (handle-command "help")
  (handle-command "look")
  (i/focus!))

(init)

;; TODO TODO TODO
;; --------------
;; - multiset instead of set for inventory?
;;   (port https://github.com/achim/multiset ?)
;;   (or have {Element Count} instead?)
