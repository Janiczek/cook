(ns cook.state.utils)

(defn empty-inventory? [state]
  (zero? (count (get-in state [:player :inventory]))))

(defn current-location [state]
  (get-in state [:player :location]))

(defn can-go? [state direction]
  (contains? (get-in state [:locations (current-location state) :directions])
             direction))

(defn is-there? [state object]
  (contains? (get-in state [:items (current-location state)])
             object))

(defn have? [state object]
  (contains? (get-in state [:player :inventory])
             object))

(defn ? [state pred]
  (get-in state [:state pred]))

(defn ! [state pred val]
  (assoc-in state [:state pred] val))

(defn go! [state direction]
  (assoc-in state [:player :location] direction))

(defn item-add!
  ([state item]          (update-in state [:player :inventory] conj item))
  ([state item location] (update-in state [:items location]    conj item)))

(defn item-remove!
  ([state item]          (update-in state [:player :inventory] disj item))
  ([state item location] (update-in state [:items location]    disj item)))

(defn item-drop! [state item]
  ;; TODO check that the user has it?
  (-> state
      (item-remove! item)                            ;; from player
      (item-add!    item (current-location state)))) ;; to the location

(defn item-pick! [state item]
  ;; TODO check that the location has it?
  (-> state
      (item-remove! item (current-location state)) ;; from the location
      (item-add!    item)))                        ;; to the player

(defn output [state string & {:keys [append?
                                     separator]
                              :or {append? false
                                   separator ""}}]
  (assoc state :output
    (if append?
      (str (get state :output) separator string)
      string)))
