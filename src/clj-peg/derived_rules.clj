(ns peg.derived-rules
  (:use peg.peg)
  (:use peg.grammar))

; optional matcher
(defn make-opt-matcher [rule]
  (make-alt-matcher [rule (make-pred-matcher (constantly true))]))

; one or more
(defn make-one-or-more-matcher [rule]
  (make-sequence-matcher [rule (make-zero-or-more-matcher rule)]))

; anything matches any single element and fails on end of input
(def anything
  (fn [input bindings]
    (when (seq input)
      {:i (rest input) :b bindings :r [(first input)]})))

; literal matcher
(defn make-literal-matcher [l]
  (make-sequence-matcher
   [(make-bind (make-rule-matcher #'anything) '-match-)
    (make-pred-matcher (fn [b] (let [i (first (b '-match-))] (= i l))))]))

; whitespace
(def whitespace
  (make-alt-matcher
   (map make-literal-matcher [\newline \space \tab])))

; string matcher
(defn make-string-matcher [s]
  (make-sequence-matcher (map make-literal-matcher s)))

(def end
  (make-not (make-rule-matcher #'anything)))