(ns cook.core
  (:require [cook.gui :refer [el-input]]
            [cook.gui.history :as h]
            [cook.gui.input   :as i]
            [cook.data.msgs :refer [intro]]
            [cook.state :refer [new-state]]
            [cook.commands :refer [do-accordingly]]))

(def state (atom {}))

(defn handle-command [state command]
  (h/append-input! command)
  (let [state-after-cmd (do-accordingly @state command)
        output (:output state-after-cmd)
        state-after-cmd (dissoc state-after-cmd :output)] (h/append! output)
    (reset! state state-after-cmd))
  (i/clear!))

(defn enter-handler [e]
  (let [enter 13
        key (or (.-which e) (.-keyCode e))]
    (when (= enter key)
      (let [content (i/content)]
        (handle-command state content)))))

(defn init [state]
  (enable-console-print!)
  (reset! state new-state)
  (aset el-input "onkeyup" enter-handler)
  (h/content! intro)
  (handle-command state "help")
  (handle-command state "look")
  (i/focus!))

(init state)
