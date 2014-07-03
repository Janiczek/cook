(ns cook.state)

(def new-state {:player {:location :castle
                         :inventory #{}}
                :state {:hopper-has-wheat-inside false
                        :flour-in-bin            false
                        :cook-has-given-quest    false
                        :cook-has-ingredients    false}
                :items {:farm      #{:egg}
                        :cow-field #{:bucket}
                        :field     #{:wheat}
                        :windmill  #{:pot}}})

(def state (atom {}))
