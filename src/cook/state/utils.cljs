(ns cook.state.utils
  (:require [cook.state :refer [state]]
            [cook.data :refer [locations]]))

(defn empty-inventory? []
  (zero? (count (get-in @state [:player :inventory]))))

(defn current-location []
  (get-in @state [:player :location]))

(defn can-go? [direction]
  (contains? (get-in locations [(current-location) :directions])
             direction))

(defn is-there? [object]
  (contains? (get-in @state [:items (current-location)])
             object))

(defn have? [object]
  (contains? (get-in @state [:player :inventory])
             object))

(defn ? [pred]
  (get-in @state [:state pred]))

(defn ! [pred val]
  (swap! state assoc-in [:state pred] val))

(defn go! [direction]
  (swap! state assoc-in [:player :location] direction))

(defn item-add!
  ([item]          (swap! state update-in [:player :inventory] conj item))
  ([item location] (swap! state update-in [:items location]    conj item)))

(defn item-remove!
  ([item]          (swap! state update-in [:player :inventory] disj item))
  ([item location] (swap! state update-in [:items location]    disj item)))

(defn item-drop! [item]
  ;; TODO check that the user has it?
  (item-remove! item)                     ;; from player
  (item-add!    item (current-location))) ;; to the location

(defn item-pick! [item]
  ;; TODO check that the location has it?
  (item-remove! item (current-location)) ;; from the location
  (item-add!    item))                   ;; to the player
