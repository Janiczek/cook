(ns cook.describe
  (:require [cook.data :refer [items-descriptions]]))

(defn describe-location [state location]
  (get-in state [:locations location :description]))

(defn describe-directions [state location]
  (apply str
    (interpose "\n"
      (vals (get-in state [:locations location :directions])))))

(defn describe-items [state location]
  (let [current-items (get-in state [:items location])
        descriptions (map #(get-in state [:locations location :items %])
                          current-items)]
    (apply str
      (interpose "\n"
        descriptions))))

(defn describe-specials [state location]
  (apply str
    (interpose "\n"
      (vals (get-in state [:locations location :specials])))))

(defn describe-npcs [state location]
  (apply str
    (interpose "\n"
      (vals (get-in state [:locations location :npcs])))))

(defn describe-inventory [state]
  (let [current-items (get-in state [:player :inventory])
        descriptions (map #(str "- " (get items-descriptions %))
                          current-items)]
    (apply str
      "Contents of your inventory:\n"
      (interpose "\n" descriptions))))
