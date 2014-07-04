(ns cook.commands.utils
  (:require [cook.state.utils :refer [current-location
                                      is-there?
                                      have?]]
            [cook.data :refer [item-synonyms]]
            [clojure.string :refer [lower-case
                                    trim
                                    replace-first]]))

(defn one-of [prefixes] ;; builds regex from seq of strings
  (re-pattern (str "("
                   (apply str (interpose "|" prefixes))
                   ")\\b")))

(defn delete [prefixes command]
  ;; deletes matching prefix from beginning of the string
  (trim (replace-first command (one-of prefixes) "")))

(defn rest-of [command prefixes] ;; words following the command
  (delete prefixes command))

(defn parse-by [command synonyms]
  (let [possible (->> synonyms
                      (map (fn [[type prefixes]]
                             (when-let [[word _] (re-find (one-of prefixes)
                                                          command)]
                               [word type])))
                      (remove nil?)
                      (into {}))]
    (or (get possible (first (re-find (one-of (keys possible)) command)))
        ;; which is first in the command
        :unknown)))

(defn prepare [command]
  (lower-case (trim command)))

(defn cmd [state command type synonyms functions]
  (let [words (rest-of command (get synonyms type))]
    ((get functions type) state words)))

(defn get-synonym [state location type]
  (get-in state [:locations location type]))

(defn find-object [state words]
  ;; TODO is this a good place for this fn?
  (let [location (current-location state)
        specials-synonyms (get-synonym state location :specials-synonyms)
        npcs-synonyms     (get-synonym state location :npcs-synonyms)
        synonyms (merge-with concat
                   (into {} (filter (fn [[item _]]
                                      (or (is-there? state item)
                                          (have? state item)))
                                    item-synonyms))
                   specials-synonyms
                   npcs-synonyms)
        object (parse-by words synonyms)
        words-without-object (delete (get synonyms object) words)]
    [object words-without-object]))
