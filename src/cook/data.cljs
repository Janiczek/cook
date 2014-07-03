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

(def locations
  {:castle
   {:description "You stand in the courtyard of the Lumbridge Castle. There's a lot of noise from all the new players respawning."
    :directions-synonyms {:lumbridge ["lumbridge"
                                      "lumb"
                                      "outside"
                                      "out"
                                      "gate"
                                      "town"
                                      "city"
                                      "east"]}
    :directions
    {:lumbridge "A gate to the east leads outside of the Lumbridge Castle into the little town of Lumbridge."}
    :npcs-synonyms {:cook ["cook"
                           "chef"]}
    :npcs
    {:cook "There is an unhappy Cook in the castle kitchen, sobbing and looking at you intently."}}

   :lumbridge
   {:description "You stand on the Lumbridge's main road, running parallel to a narrow stream of water."
    :directions-synonyms {:castle ["entrance"
                                   "castle"
                                   "cook"
                                   "kitchen"
                                   "west"]
                          :farm ["farm"
                                 "north"
                                 "road"]
                          :cow-field ["cow field"
                                      "field"
                                      "cows"
                                      "bridge"
                                      "east"
                                      "river"
                                      "across"]}
    :directions
    {:castle "An entrance to the Lumbridge castle can be seen to the west." 
     :farm "A road to the north leads to a farm."
     :cow-field "Just across the bridge over River Lum on the east there is a small cow field."}}

   :farm
   {:description "You stand on the road near a small farm. There are chicken behind the fence."
    :directions-synonyms {:lumbridge ["south"
                                      "lumbridge"
                                      "lumb"
                                      "town"
                                      "city"]
                          :field ["field"
                                  "windmill"
                                  "mill"
                                  "wheat"
                                  "west"]}
    :directions
    {:lumbridge "A road to the south leads back to the town of Lumbridge."
     :field "To the west of you the road continues to a field of wheat with a windmill nearby."}
    :items
    {:egg "You see a super large egg laying near one of the chicken. Wow, good job, chicken!"}}

   :cow-field
   {:description "You stand inside Lumbridge's cow field. Surprisingly, there are a lot of cows."
    :directions-synonyms {:lumbridge ["town"
                                      "city"
                                      "lumbridge"
                                      "lumb"
                                      "bridge"
                                      "west"
                                      "across"]}
    :directions
    {:lumbridge "The town of Lumbridge is just across the bridge to the west."}
    :items
    {:bucket "You see a bucket. It might come in handy for holding something liquid."}
    :specials-synonyms {:cow ["dairy cow"
                              "cow"]}
    :specials
    {:cow "There is a cow tied to the fence. It seems it's dairy."}}

   :field
   {:description "You stand in a field of wheat."
    :directions-synonyms {:farm ["farm"
                                 "east"
                                 "road"]
                          :windmill ["windmill"
                                     "mill"
                                     "door"
                                     "inside"
                                     "in"]}
    :directions
    {:farm "A road to the east leads to a farm."
     :windmill "An open front door invites you inside the windmill."}
    :items
    {:wheat "There is wheat as far as the eye can see... Want to pick some?"}}

   :windmill
   {:description "You stand inside a windmill."
    :directions-synonyms {:field ["field"
                                  "wheat"
                                  "outside"
                                  "out"]
                          :upstairs ["stairs"
                                     "staircase"
                                     "upstairs"
                                     "up"
                                     "top"
                                     "windmill"]}
    :directions
    {:field "A field of wheat waits for you just outside the door."
     :upstairs "A staircase leads upstairs to the top of the windmill."}
    :items
    {:pot "A pot lies on the table. It can hold whatever you put in it."}
    :specials-synonyms {:bin ["bin"
                              "container"
                              "holder"]}
    :specials
    {:bin "There is a bin into which the ground flour falls from the machinery above it."}}

:upstairs
{:description "You stand on the top of the windmill."
 :directions-synonyms {:windmill ["downstairs"
                                  "down"
                                  "stairs"
                                  "staircase"
                                  "back"
                                  "windmill"
                                  "mill"]}
 :directions
 {:windmill "A staircase leads back to the windmill ground floor."}
 :specials-synonyms {:hopper ["hopper"
                              "funnel"
                              "feeder"
                              "container"]
                     :controls ["controls"
                                "control"
                                "levers"
                                "lever"
                                "buttons"
                                "button"]}
 :specials
 {:hopper "There is a hopper, waiting for you to put some grains inside."
  :controls "There are windmill controls operating the hopper. You should probably fiddle with them after putting some wheat into the hopper."}}})
