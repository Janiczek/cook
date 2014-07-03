(ns cook.commands
  (:require [cook.utils.commands :refer [prepare
                                         cmd
                                         parse-by
                                         find-object]]
            [cook.utils.state :refer [empty-inventory?
                                      current-location
                                      can-go?
                                      have?
                                      is-there?
                                      go!
                                      item-pick!
                                      item-add!
                                      item-remove!
                                      ?
                                      !]]
            [cook.data :refer [cmd-synonyms
                               item-synonyms
                               locations]]
            [cook.data.msgs :as msg]
            [cook.data.quest :as q]
            [cook.describe :refer [describe-inventory
                                   describe-location
                                   describe-directions
                                   describe-items
                                   describe-specials
                                   describe-npcs]]
            [cook.state :refer [state]])
  (:refer-clojure :exclude [use]))

(defmulti talk
  (fn [words]
    (let [location (current-location)
          npcs-synonyms (get-in locations [location :npcs-synonyms])]
      (parse-by words npcs-synonyms))))

(defmethod talk :unknown [_]
  msg/talk-unknown)

(defmethod talk :cook [_]
  (cond
    (not (? :cook-has-given-quest))
    (do
      (! :cook-has-given-quest true)
      q/start)

    (and (? :cook-has-given-quest)
         (not (? :cook-has-ingredients)))
    (let [have-all-ingredients? (and (have? :bucket-of-milk)
                                     (have? :egg)
                                     (have? :pot-of-flour))
          log (str q/cook-ingredients?
                   "\n\n"
                   (if have-all-ingredients?
                     q/yes-complete
                     q/not-yet))]
      (when have-all-ingredients?
        (do
          (item-remove! :bucket-of-milk)
          (item-remove! :egg)
          (item-remove! :pot-of-flour)
          (! :cook-has-ingredients true)))
      log)
    
    :else
    q/after-quest))

(defmulti use-it identity)

(defmethod use-it :controls [_]
  (cond
    (and (? :hopper-has-wheat-inside)
         (not (? :flour-in-bin)))
    (do
      (! :hopper-has-wheat-inside false)
      (! :flour-in-bin true)
      (str q/controls-start q/controls-done))

    (not (? :hopper-has-wheat-inside))
    (str q/controls-start q/controls-whoops)))

(defmulti use-on vector)

(defmethod use-on [:wheat :hopper] [_ _]
  (cond
    (not (? :hopper-has-wheat-inside))
    (do
      (! :hopper-has-wheat-inside true)
      (item-remove! :wheat)
      q/hopper-put)
    
    :else
    q/hopper-full))

(defmethod use-on [:bucket :cow] [_ _]
  (cond
    (have? :bucket)
    (do
      (item-remove! :bucket)
      (item-add!    :bucket-of-milk)
      q/cow-milk)
    
    :else
    q/cow-no-bucket))

(defmethod use-on [:pot :bin] [_ _]
  (cond
    (? :flour-in-bin)
    (do
      (! :flour-in-bin false)
      (item-remove! :pot)
      (item-add!    :pot-of-flour)
      q/bin-take-flour)
    
    :else
    q/bin-empty))

(defn use [words]
  (let [[object rest-of-words] (find-object words)
        [subject _]            (find-object rest-of-words)]
    (cond
      (and (not= object :unknown)
           (not= subject :unknown))
      (try
        (use-on object subject)
        (catch js/Error e msg/wont-work))

      (and (not= object :unknown)
           (= subject :unknown))
      (try
        (use-it object)
        (catch js/Error e msg/wont-work))

      :else
      msg/cant-use)))

(defn look
  ([]
   (apply str
     (interpose "\n"
       (let [location (current-location)]
         (filter (complement empty?)
                 [(describe-location   location locations)
                  (describe-directions location locations)
                  (describe-npcs       location locations)
                  (describe-specials   location locations)
                  (describe-items      location locations state)])))))
  ([_] (look)))

(defn go [words]
  (let [location     (current-location)
        dir-synonyms (get-in locations [location :directions-synonyms])
        direction    (parse-by words dir-synonyms)
        exists-path? (can-go? direction)]
    (cond
      (not exists-path?)     msg/cant-go
      (= location direction) msg/already-there
      :else                  (do (go! direction)
                                 (look)))))


(defn inventory
  ([]
   (if (empty-inventory?)
     msg/empty-inventory
     (describe-inventory state)))
  ([_] (inventory)))

(defn pick [words]
  (let [item     (parse-by words item-synonyms)
        location (current-location)]
    (cond
      (not (is-there? item))
      msg/cant-pick

      :else
      (do
        (item-pick! item)
        (str msg/picked-up "\n"
             (inventory))))))

(defn help [_]
  msg/help)

(defn unknown [_]
  msg/unknown)

(def functions {:talk      talk
                :go        go
                :pick      pick
                :use       use
                :look      look
                :inventory inventory
                :help      help
                :unknown   unknown})

(defn do-accordingly [command]
  (let [command (prepare command)]
    (cmd command
         (parse-by command cmd-synonyms)
         cmd-synonyms
         functions)))
