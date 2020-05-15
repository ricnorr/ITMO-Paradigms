

         (defn -return [value tail] {:value value :tail tail})
         (def -valid? boolean)
         (def -value :value)
         (def -tail :tail)


         (defn _show [result]
           (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                                "!"))
         (defn tabulate [parser inputs]
           (run! (fn [input] (printf "    %-10s %s\n" input (_show (parser input)))) inputs))


(defn _empty [value] (partial -return value))      ;возвращает nil и саму строку как хвост
(tabulate (_empty nil) ["" "~"])

(defn _char [p]                                    ; p - grammar
  (fn [[c & cs]]                                   ;получает строку, берёт только первый эл-т
    (if (and c (p c)) (-return c cs))))            ; если первый символ существует и удовлетворяет алфавиту

         (tabulate (_char #{\a \b \c}) ["ax" "by" "" "a" "x" "xa"])


         (defn _map [f result]                              ;не особо понял зачем
           (if (-valid? result)
             (-return (f (-value result)) (-tail result))))
         (tabulate (comp (partial _map clojure.string/upper-case) (_char #{\a \b \c})) ["a" "a~" "b" "b~" "" "x" "x~"])



         (defn _combine [f a b]
           (fn [str]
             (let [ar ((force a) str)]
               (if (-valid? ar)
                 (_map (partial f (-value ar))
                       ((force b) (-tail ar))))))
           )

         (tabulate (_combine str (_char #{\a \b}) (_char #{\x})) ["ax" "ax~" "bx" "bx~" "" "a" "x" "xa"])
         ;схаваем "ax" хвост тильда
         (defn _either [a b]                                ; применяем первый, не получилось применяем второй
           (fn [str]
             (let [ar ((force a) str)]
               (if (-valid? ar) ar ((force b) str)))))

         (tabulate (_either (_char #{\a}) (_char #{\b})) ["ax" "ax~" "bx" "bx~" "" "a" "x" "xa"])
; схаваем a либо b


         (defn _parser [p]
           (fn [input]
             (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))

         (mapv (_parser (_combine str (_char #{\a \b}) (_char #{\x}))) ["ax" "ax~" "bx" "bx~" "" "a" "x" "xa"])
; парсим сначала первым, затем вторым по хвосту, оставшеемся бросим