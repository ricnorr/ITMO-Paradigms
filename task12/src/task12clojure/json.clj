(chapter "JSON parser")

(section "Simple parsers")

        (defn -return [value tail] {:value value :tail tail})

        (defn _map [f result] ; взять функцию от head, tail не трогаем
          (println result)
          (if (-valid? result)
            ((println "if-yes") (-return (f (-value result)) (-tail result)))))

         (defn +map [f parser] (comp (partial _map f) parser))
         ;композиция функций, закинем строку, распарсит, еще и функцию к head
         ;применит

        (defn _char [p]                                    ; p - grammar
          (fn [[c & cs]] ;получает строку, берёт только первый эл-т
             (println "charing") (if (and c (p c)) (-return c cs))))

        ((_char #{\a \b \c}) "ax")
        => {:value \a, :tail (\x)}

        (defn +char [chars] (_char (set chars)))
        ((+char "abc") "abc")
        => {:value \a, :tail (\b \c)}

        (defn +str [p] (+map (partial apply str) p))
; распарсит по p, распаршенное список, сделает из head'a строчку

        (+str ((+char "abc")) "abc")                ; не работает; проблема в мап

        (defn _either [a b]                                 ;если первый не нил, то выкидываем
        ;иначе второй парсер  например _char выкинет нил если первый символ не подходит ок да
          (fn [str]
            (let [ar ((force a) str)]
              (if (-valid? ar) ar ((force b) str)))))

        (defn +or [p & ps]
          (reduce _either p ps))     ; как either только на много парсеров

        (def +ignore (partial +map (constantly 'ignore)))   ; закинем парсер, строку
; распарсит ; то что распарсил выкинет игнор

        (defn iconj [coll value]                ; присобачим value если не инор
          (if (= value 'ignore) coll (conj coll value)))

(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]                               ;парсим str по a
      (if (-valid? ar)                                      ;если a не нил, применить
        (_map (partial f (-value ar))                       ; функцию f(head по a, (tail по а) по б) = t
              ((force b) (-tail ar))))))                    ; вернули {t, tail по а tail по б}

  ((_combine str (+char "a") (+char "b")) "abc")
  => {:value "ab", :tail (\c)}

  (defn _empty [value] (partial -return value))

  (defn +seq [& ps]
    (reduce (partial _combine iconj) (_empty []) ps))       ; (_empty []) = {[], arg} ; ps - куча строк
  ;(partial _combine iconj) (_empty []) ps) (+char)  | [(+ char) str] => out {[(+ char) str], tail}
  ;засовывам парсеры и дрочим строку этими парсерами
  ((+seq (+char "a") (+char "b") (+char "c")) "abc")
  => {:value [\a \b \c], :tail nil}

  (defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps))) ;применим к head из seq функцию



        (defn +star [p] ;(cons [1 2] [4 5 6]) = ([1 2] [4 5 6])         p - парсер
          (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
  ; повторяет столько раз, пока возможно

       (defn +plus [p] (+seqf cons p (+star p)))
       ;парсануть парсером как минимум один раз


        (def *digit (+char "0123456789"))
        (def *number (+map read-string (+str (+plus *digit)))) ; парсим пока цифорка

         (tabulate *number ["1" "1~" "12~" "123~" "" "~"])
; 1          -> 1 | ""
;    1~         -> 1 | "~"
;    12~        -> 12 | "~"
;    123~       -> 123 | "~"
;               !
;    ~          !
;


(example "*string"
         (def *string
           (+seqn 1 (+char "\"") (+str (+star (+char-not "\""))) (+char "\"")))
         (tabulate *string ["x" "\"\"" "\"" "\"ab\"" "\"ab\"~"]))
; x          !
; ""         -> "" | ""
; "          !
;    "ab"       -> "ab" | ""
;    "ab"~      -> "ab" | "~"
;
;
;
;
;

(example "*ws: whitespace"
         (def *space (+char " \t\n\r"))

         (def *ws (+ignore (+star *space)))

         (*ws "            ds")
         => {:value ignore, :tail (\d \s)}

         (tabulate *ws ["" "~" "     ~" "\t~"]))



;              -> ignore | ""
;    ~          -> ignore | "~"
;         ~     -> ignore | "~"
;    	~         -> ignore | "~"
;
;
;
;

(example "*null: null literal"
         (def *null (+seqf (constantly 'null) (+char "n") (+char "u") (+char "l") (+char "l")))(tabulate *null ["null" "null~" "nll" ""]))
(example "*identifier"
         (def *all-chars (mapv char (range 32 128)))
         (apply str *all-chars)
         (def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))
         (tabulate *letter ["a" "A" "1" ""])
         (def *identifier (+str (+seqf cons *letter (+star (+or *letter *digit))))) ;вытянем буковку, дальше парашу, и притянем другое
         (tabulate *identifier ["a" "A" "1" "A1" "a1~"]))

  (def *var (+str (+star *letter)))
;    a          -> "a" | ""
;    A          -> "A" | ""
;    1          !
;    A1         -> "A1" | ""
;    a1~        -> "a1" | "~"
;
;
;
;

(section "Array")
(example "One element"
         (defn *array [p]
           (+seqn 1 (+char "[") p (+char "]")))
         (tabulate (*array *number) ["[]" "[1]" "[1,2]" "[1,2,3]" "[1, 2]" "[ 1 , 2 , 3 ]" "[1,2]~" "123" ""]))
(example "One optional element"
         (defn *array [p]
           (+seqn 1 (+char "[") (+opt p) (+char "]")))
         (tabulate (*array *number) ["[]" "[1]" "[1,2]" "[1,2,3]" "[1, 2]" "[ 1 , 2 , 3 ]" "[1,2]~" "123" ""]))
(example "Multiple elements preview"
         (defn *array [p]
           (+seqn 1 (+char "[") (+opt (+seq p (+star (+seqn 1 (+char ",") p)))) (+char "]")))
         (tabulate (*array *number) ["[]" "[1]" "[1,2]" "[1,2,3]" "[1, 2]" "[ 1 , 2 , 3 ]" "[1,2]~" "123" ""]))
(example "Multiple elements"
         (defn *array [p]
           (+seqn 1 (+char "[") (+opt (+seqf cons p (+star (+seqn 1 (+char ",") p)))) (+char "]")))
         (tabulate (*array *number) ["[]" "[1]" "[1,2]" "[1,2,3]" "[1, 2]" "[ 1 , 2 , 3 ]" "[1,2]~" "123" ""]))
(example "Whitespace handling"
         (defn *array [p]
           (+seqn 1 (+char "[") (+opt (+seqf cons *ws p (+star (+seqn 1 *ws (+char ",") *ws p)))) *ws (+char "]")))
         (tabulate (*array *number) ["[]" "[1]" "[1,2]" "[1,2,3]" "[1, 2]" "[ 1 , 2 , 3 ]" "[1,2]~" "123" ""]))

         (defn *seq [begin p end]
           (+seqn 1 (+char begin) (+opt (+seqf cons *ws p (+star (+seqn 1 *ws (+char ",") *ws p)))) *ws (+char end)))

         (defn *array [p] (*seq "[" p "]"))

         (tabulate (*array *number) ["[]" "[1]" "[1,2]" "[1,2,3]" "[1, 2]" "[ 1 , 2 , 3 ]" "[1,2]~" "123" ""])



         (defn *member [p] (+seq *identifier *ws (+ignore (+char ":")) *ws p))

         (tabulate (*member *number) ["a:2" "a: 2" "a : 2", "a : " "2 : 2"]))

         (defn *object [p] (*seq "{" (*member p) "}"))


         (tabulate (*object *number) ["123" "{}" "{a:1}" "{a : 1 , boo : 2}"]))

         (defn *object [p] (+map (partial reduce #(apply assoc %1 %2) {}) (*seq "{" (*member p) "}")))
         (tabulate (*object *number) ["123" "{}" "{a:1}" "{a : 1 , boo : 2}"])


(section "JSON")
(example "Partial"
         (def json
           (letfn [(*value []
                     (delay (+or
                              *null
                              *number
                              *string
                              (*object (*value))
                              (*array (*value)))))]
             (+parser (+seqn 0 *ws (*value)))))
         (json "[1, {a: \"hello\", b: [1, 2, 3]}, null]")
         (json "[1, {a: \"hello\", b: [1, 2, 3]}, null]~"))

(example "Complete"
         (def json
           (let
             [*null (+seqf (constantly 'null) (+char "n") (+char "u") (+char "l") (+char "l"))
              *all-chars (mapv char (range 0 128))
              *letter (+char (apply str (filter #(Character/isLetter %) *all-chars)))
              *digit (+char (apply str (filter #(Character/isDigit %) *all-chars)))
              *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars)))
              *ws (+ignore (+star *space))
              *number (+map read-string (+str (+plus *digit)))
              *identifier (+str (+seqf cons *letter (+star (+or *letter *digit))))
              *string (+seqn 1 (+char "\"") (+str (+star (+char-not "\""))) (+char "\""))]
             (letfn [(*seq [begin p end]
                       (+seqn 1 (+char begin) (+opt (+seqf cons *ws p (+star (+seqn 1 *ws (+char ",") *ws p)))) *ws (+char end)))
                     (*array [] (*seq "[" (delay (*value)) "]"))
                     (*member [] (+seq *identifier *ws (+ignore (+char ":")) *ws (delay (*value))))
                     (*object [] (+map (partial reduce #(apply assoc %1 %2) {}) (*seq "{" (*member) "}")))
                     (*value [] (+or *null *number *string (*object) (*array)))]
               (+parser (+seqn 0 *ws (*value))))))
         (json "[1, {a: \"hello\", b: [1, 2, 3]}, null]")
         (json "[1, {a: \"hello\", b: [1, 2, 3]}, null]~"))