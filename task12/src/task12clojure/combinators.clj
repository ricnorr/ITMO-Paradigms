
         (defn +char [chars] (_char (set chars)))
         (tabulate (+char "abc") ["ab" "a~" "b" "b~" "" "x" "x~"])
; распарсит a и хвост оставит


         (defn +char-not [chars] (_char (comp not (set chars))))
         (tabulate (+char-not "abc") ["a" "a~" "b" "b~" "" "x" "x~"])
; b          !
;    b~         !
;               !
;    x          -> \x | ""
;    x~         -> \x | "~"
;
;
;


         (defn +map [f parser] (println parser) (comp (partial _map f) parser))
         (tabulate (+map clojure.string/upper-case (+char "abc")) ["a" "a~" "b" "b~" "" "x" "x~"])
; применит апперкейс
;
;



         (def +parser _parser)



         (def +ignore (partial +map (constantly 'ignore)))
         (tabulate (+ignore (+char "abc")) ["a" "a~" "b" "b~" "" "x" "x~"])

;   a          -> ignore | ""
;   a~         -> ignore | "~"
;   b          -> ignore | ""
;   b~         -> ignore | "~"
;               !
;   x          !
;   x~         !
;



         (defn iconj [coll value]
           (if (= value 'ignore) coll (conj coll value)))
         (defn +seq [& ps]
           (reduce (partial _combine iconj) (_empty []) ps))
         (tabulate (+seq (+char "abc") (+ignore (+char "xyz")) (+char "ABC")) ["axA~" "axA" "aXA~"])

;axA~       -> [\a \A] | "~"
;axA        -> [\a \A] | ""
;aXA~       !



         (defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
         (tabulate (+seqf str (+char "abc") (+ignore (+char "xyz")) (+char "ABC")) ["axA~" "axA" "aXA~"])
;  axA~       -> "aA" | "~"
;  axA        -> "aA" | ""
;  aXA~       !

         (defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))
         (tabulate (+seqn 1 (+char "abc") (+ignore (+char "xyz")) (+char "ABC")) ["axA~" "axA" "aXA~"])

; axA~       -> \A | "~"
; axA        -> \A | ""
; aXA~       !




         (defn +or [p & ps]
           (reduce _either p ps))
         (tabulate (+or (+char "a") (+char "b") (+char "c")) ["ab" "a~" "b" "b~" "" "x" "x~"])
; a          -> \a | ""
;    a~         -> \a | "~"
;    b          -> \b | ""
;    b~         -> \b | "~"
;               !
;    x          !
;    x~         !


         (defn +opt [p]
           (+or p (_empty nil)))
         (tabulate (+opt (+char "a")) ["a" "a~" "aa" "dabc" "aa~" "" "~"])
;    a          -> \a | ""
;    a~         -> \a | "~"
;    aa         -> \a | "a"
;    aa~        -> \a | "a~"
;               -> nil | ""
;    ~          -> nil | "~"
(def *exp (*seqn 0 *ws (+or parse_easy_exp *bracket_exp)))


         (defn +star [p]
           (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
         (tabulate (+star (+char "a")) ["a" "a~" "aa" "aa~" "" "~"])

;a          -> (\a) | ""
;a~         -> (\a) | "~"
;aa         -> (\a \a) | ""
;aa~        -> (\a \a) | "~"
;   -> () | ""
; ~          -> () | "~"


         (defn +plus [p] (+seqf cons p (+star p)))
         (tabulate (+plus (+char "a")) ["a" "a~" "aa" "aa~" "" "~"])
; a          -> (\a) | ""
; a~         -> (\a) | "~"
; aa         -> (\a \a) | ""
; aa~        -> (\a \a) | "~"
;              !
;  ~          !
;



         (defn +str [p] (+map (partial apply str) p))
         (tabulate (+str (+star (+char "a"))) ["a" "a~" "aa" "aa~" "" "~"])
; a          -> "a" | ""
;    a~         -> "a" | "~"
;    aa         -> "aa" | ""
;    aa~        -> "aa" | "~"
;               -> "" | ""
;    ~          -> "" | "~"
F