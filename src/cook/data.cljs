(ns cook.data)

(def item-synonyms {:wheat ["wheat"
                            "grain"
                            "weed"]

                    :pot ["pot"]

                    :bucket ["bucket"]

                    :egg ["egg"]

                    ;; ---------------------------------------
                    ;; TODO what if the shorter item gets picked first? 
                    ;; pot       instead of    pot of flour
                    ;; bucket    instead of    bucket of milk

                    :pot-of-flour ["pot of flour"
                                   "flour pot"
                                   "flour"] 

                    :bucket-of-milk ["bucket of milk"
                                     "milk bucket"
                                     "milk"]})

(def cmd-synonyms {:talk ["talk with" 
                          "talk to"
                          "talk"]

                   :go ["go"
                        "walk"
                        "run"] 

                   :pick ["pick up"
                          "pickup"
                          "pick"
                          "take"
                          "get"]

                   :use ["use"]

                   :look ["look"
                          "see"
                          "watch"]

                   :inventory ["inventory"
                               "inv"
                               "backpack"
                               "items"
                               "pocket"
                               "pockets"]

                   :help ["help"
                          "wtf"
                          "what"
                          "rtfm"]})

(def items-descriptions ;; for the inventory
  {:wheat          "A freshly picked wheat."
   :pot            "An empty pot."
   :bucket         "An empty bucket."
   :egg            "An egg. Careful!"
   :pot-of-flour   "A pot of flour."
   :bucket-of-milk "A bucket of the finest Lumbridge milk."})
