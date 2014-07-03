(defproject cook "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2261"]]
  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]
  :source-paths ["src"]
  :cljsbuild { 
    :builds [{:id "dev"
              :source-paths ["src"]
              :compiler {
                :output-to "cook2.js"
                :output-dir "out2"
                :optimizations :none
                :source-map true}}
             {:id "prod"
              :source-paths ["src"]
              :compiler {
                :output-to "cook.js"
                :output-dir "out"
                :optimizations :advanced
                :pretty-print false
                :source-map "cook.js.map"}}]})
