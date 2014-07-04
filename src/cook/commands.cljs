(ns cook.commands
  (:require-macros [pallet.thread-expr :refer [when->]])
  (:require [cook.commands.utils :refer [prepare
                                         cmd
                                         parse-by
                                         get-synonym
                                         find-object]]
            [cook.state.utils :refer [empty-inventory?
                                      current-location
                                      can-go?
                                      have?
                                      is-there?
                                      go!
                                      item-pick!
                                      item-add!
                                      item-remove!
                                      ?
                                      !
                                      output]]
            [cook.data :refer [cmd-synonyms
                               item-synonyms]]
            [cook.data.msgs :as msg]
            [cook.data.quest :as q]
            [cook.describe :refer [describe-inventory
                                   describe-location
                                   describe-directions
                                   describe-items
                                   describe-specials
                                   describe-npcs]])
  (:refer-clojure :exclude [use]))

(defmulti talk
  (fn [state words] ;; command -> :cook
    (let [location (current-location state)
          npcs-synonyms (get-synonym state location :npcs-synonyms)]
      (parse-by words npcs-synonyms))))

(defmethod talk :unknown [state _]
  (output state msg/talk-unknown))

(defmethod talk :cook [state _]
  (cond
    (not (? state :cook-has-given-quest))
    (-> state
        (! :cook-has-given-quest true)
        (output q/start))

    (and (? state :cook-has-given-quest)
         (not (? state :cook-has-ingredients)))
    (let [have-all-ingredients? (and (have? state :bucket-of-milk)
                                     (have? state :egg)
                                     (have? state :pot-of-flour))]
      (-> state
          (when-> have-all-ingredients?
            (item-remove! :bucket-of-milk)
            (item-remove! :egg)
            (item-remove! :pot-of-flour)
            (! :cook-has-ingredients true))
          (output (str q/cook-ingredients?
                       "\n\n"
                       (if have-all-ingredients?
                         q/yes-complete
                         q/not-yet)))))
    
    :else
    (output state q/after-quest)))

(defmulti use-it (fn [state object] object))

(defmethod use-it :controls [state _]
  (if (and (? state :hopper-has-wheat-inside)
           (not (? state :flour-in-bin)))
    (-> state
        (! :hopper-has-wheat-inside false)
        (! :flour-in-bin true)
        (output (str q/controls-start q/controls-done)))
    (output state (str q/controls-start q/controls-whoops))))

(defmulti use-on (fn [state & rest] rest)) ; (use-on state x1 x2) -> [x1 x2]

(defmethod use-on [:wheat :hopper] [state _ _]
  (if (not (? state :hopper-has-wheat-inside))
    (-> state
        (! :hopper-has-wheat-inside true)
        (item-remove! :wheat)
        (output q/hopper-put))
    (output state q/hopper-full)))

(defmethod use-on [:bucket :cow] [state _ _]
  (if (have? state :bucket)
    (-> state
        (item-remove! :bucket)
        (item-add!    :bucket-of-milk)
        (output q/cow-milk))
    (output state q/cow-no-bucket)))

(defmethod use-on [:pot :bin] [state _ _]
  (if (? state :flour-in-bin)
    (-> state
        (! :flour-in-bin false)
        (item-remove! :pot)
        (item-add!    :pot-of-flour)
        (output q/bin-take-flour))
    (output state q/bin-empty)))

(defn use [state words]
  (let [[object rest-of-words] (find-object state words)
        [subject _]            (find-object state rest-of-words)]
    (cond
      (and (not= object :unknown)
           (not= subject :unknown))
      (try
        (use-on state object subject)
        (catch js/Error e (output state msg/wont-work)))

      (and (not= object :unknown)
           (= subject :unknown))
      (try
        (use-it state object)
        (catch js/Error e (output state msg/wont-work)))

      :else
      (output state msg/cant-use))))

(defn look-string [state]
  (apply str
    (interpose "\n"
      (let [location (current-location state)]
        (filter (complement empty?)
                [(describe-location   state location)
                 (describe-directions state location)
                 (describe-npcs       state location)
                 (describe-specials   state location)
                 (describe-items      state location)])))))

(defn look
  ([state]   (output state (look-string state)))
  ([state _] (look state))) ;; ignore any words after

(defn go [state words]
  (let [location     (current-location state)
        dir-synonyms (get-synonym state location :directions-synonyms)
        direction    (parse-by words dir-synonyms)
        exists-path? (can-go? state direction)]
    (if (not exists-path?)
      (output state msg/cant-go)
      (let [new-state (go! state direction)]
        (output new-state (look-string new-state))))))

(defn inventory
  ([state]
   (if (empty-inventory? state)
     (output state msg/empty-inventory)
     (output state (describe-inventory state))))
  ([state _] (inventory state)))

(defn pick [state words]
  (let [item     (parse-by words item-synonyms)
        location (current-location state)]
    (if (not (is-there? state item))
      (output state msg/cant-pick) 
      (let [new-state (-> state
                          (item-pick! item)
                          (output msg/picked-up))]
        (output new-state (describe-inventory new-state)
                :append? :true
                :separator "\n")))))

(defn help [state _]
  (output state msg/help))

(defn unknown [state _]
  (output state msg/unknown))

(def functions {:talk      talk
                :go        go
                :pick      pick
                :use       use
                :look      look
                :inventory inventory
                :help      help
                :unknown   unknown})

(defn do-accordingly [state command] ;; from here on it's deref'd
  (let [command (prepare command)]
    (cmd state
         command
         (parse-by command cmd-synonyms)
         cmd-synonyms
         functions)))
