(ns cook.describe
  (:require [cook.data :refer [items-descriptions]]))

(defn describe-location [location locations]
  (get-in locations [location :description]))

(defn describe-directions [location locations]
  (apply str
    (interpose "\n"
      (vals (get-in locations [location :directions])))))

(defn describe-items [location locations state]
  (let [current-items (get-in @state [:items location])
        descriptions (map #(get-in locations [location :items %])
                          current-items)]
    (apply str
      (interpose "\n"
        descriptions))))

(defn describe-specials [location locations]
  (apply str
    (interpose "\n"
      (vals (get-in locations [location :specials])))))

(defn describe-npcs [location locations]
  (apply str
    (interpose "\n"
      (vals (get-in locations [location :npcs])))))


(defn describe-inventory [state]
  (let [current-items (get-in @state [:player :inventory])
        descriptions (map #(str "- " (get items-descriptions %))
                          current-items)]
    (apply str
      "Contents of your inventory:\n"
      (interpose "\n" descriptions))))
