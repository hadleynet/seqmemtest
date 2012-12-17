(ns seqmemtest.core
  (:gen-class)
  (:use [clojure-csv.core :only (parse-csv)]) 
  (:use [clojure.set :only (map-invert)]) 
  (:use [clojure.string :only (split)]) 
  (:use [clojure.tools.cli :only (cli)])
  (:import [edu.columbia.stat.wood.pub.sequencememoizer IntSequenceMemoizerParameters IntSequenceMemoizer])
  (:require [clojure.java.io :as io :only (reader)])
)

(defn memoizer
  "Create a new memoizer for a specified number words distinct elements"
  [nElements]
  (let [ismp (IntSequenceMemoizerParameters. (+ 1 nElements))]
    (IntSequenceMemoizer. ismp)))

(defn make-index
  "Index a collection of words, returning a map of words to index"
  [words]
  (zipmap (set words) (range)))
  
(defn map-words-to-index
  "Map each supplied word to its corresponding integer index"
  [words index]
  (map #(index %) words))
  
(defn map-index-to-words
  "Map each index to its corresponding word"
  [indices index]
  (let [rev-index (map-invert index)]
    (map #(rev-index %) indices)))

(defn -main
  "Read a sequence"
  [& args]
  (let [[options args banner] (cli args
                                   ["-h" "--help" "Show help" :default false :flag true]
                                   ["-f" "--file" "A CSV containing one line per sequence"]
                                   ["-s" "--sequence" "The sequence to memoize" :default [] :parse-fn #(split % #"\s")]
                                   ["-n" "--number" "The number of samples to generate" :default 5 :parse-fn #(Integer. %)]
                                   ["-c" "--context" "The context from which to begin generating samples" :default [] :parse-fn #(split % #"\s")]
                                   ["-i" "--iterations" "The number of sampling iterations when generating the model" :default 100 :parse-fn #(Integer. %)])]
    (when (:help options)
      (println banner)
      (System/exit 0))
      
    (let [sequences (if (:file options) (parse-csv (io/reader (:file options))) [(:sequence options)])
          unique-words (reduce into #{} (conj sequences (:context options)))
          index (make-index unique-words)
          n (:number options)
          context (map-words-to-index (:context options) index)
          sm (memoizer (count index))]
      (doseq [sequence sequences]
        (.newSequence sm)
        (.continueSequence sm (int-array (map-words-to-index sequence index)))
      )
      
      (.sample sm (:iterations options))
      (let [generated-sequence (.generateSequence sm (int-array context) n)
            generated-words (map-index-to-words (into [] generated-sequence) index)]
        (println generated-words))
    )))
