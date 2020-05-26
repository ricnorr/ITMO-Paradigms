;;;;;;;STD LIBRARY;;;;;;;;;;;;;;
(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)
(defn _show [result]
  (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                       "!"))
(defn tabulate [parser inputs]
  (run! (fn [input] (printf "    %-10s %s\n" input (_show (parser input)))) inputs))
(defn _empty [value] (partial -return value))
(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
(defn _map [f result]
  (if (-valid? result)
    (-return (f (-value result)) (-tail result))))
(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        (_map (partial f (-value ar))
              ((force b) (-tail ar)))))))
(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))
(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))
(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (partial _map f) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly 'ignore)))
(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))
(defn *word [word] (apply +seq (reduce #(conj %1 (+char (str %2))) [] word)))
(defn +or [p & ps]
  (reduce _either p ps))
(defn +opt [p]
  (+or p (_empty nil)))
(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))
(def *digit (+char "0123456789"))
(def *all-chars (mapv char (range 32 128)))
(apply str *all-chars)
(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))
(def *ob (+ignore (+char "(")))
(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))


;;;;;;;;END OF STD LIBRARY;;;;;;;;;



(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :proto) (proto-get (obj :proto) key)))

(defn proto-call [obj key]
  (proto-get obj key))

(def opProto)

(defn constructor [proto func differ token]
  (fn [& vals] {:proto proto :vals vals :func func :differ differ :token token}))

(defn method [key] (fn [obj & args] (apply (proto-call obj key) obj args)))

(def evaluate (method :evaluate))
(def diff (method :diff))
(def toStringSuffix (method :toString))
(def getVals #(proto-get %1 :vals))
(def getToken #(proto-get %1 :token))



(def opProto
  {:evaluate
   (fn [obj args] (apply (get obj :func) (mapv #(evaluate %1 args) (get obj :vals))))
   :diff
   (fn [obj args] (let [vals (getVals obj)
                        diff_vals (mapv #(diff %1 args) vals)]
                    (apply (get obj :differ) (concat vals diff_vals))
                    ))
   :toString
   (fn [obj] (str "(" (clojure.string/join " " (mapv toStringSuffix (getVals obj))) " " (getToken obj) ")"))
   })



(def Constant)

(def constProto
  {:evaluate
   (fn [obj & args] (first (getVals obj)))
   :toString
   (fn [obj] (format "%.1f" (double (first (getVals obj)))))
   :diff
   (fn [& args] (Constant 0))
   })

(def variableProto
  {:evaluate
   (fn [obj args] (get args (str (first (getVals obj))) nil))
   :diff
   (fn [obj args] (if (= args (first (getVals obj))) (Constant 1) (Constant 0)))
   :toString
   (fn [obj] (str (first (getVals obj))))
   })

(def Add (constructor opProto + #(Add %3 %4) "+"))
(def Subtract (constructor opProto - #(Subtract %3 %4) "-"))
(def Multiply (constructor opProto * #(Add (Multiply %3 %2) (Multiply %4 %1)) "*"))
(def Divide (constructor opProto #(/ (double %1) (double %2)) #(Divide (Subtract (Multiply %3 %2) (Multiply %1 %4)) (Multiply %2 %2)) "/"))
(def Negate (constructor opProto #(- %1) #(Negate %2) "negate"))
(def Constant (constructor constProto nil nil nil))
(def Variable (constructor variableProto nil nil nil))
(def Pow (constructor opProto #(Math/pow %1 %2) nil "**"))
(def Log (constructor opProto #(/ (Math/log (Math/abs %2)) (Math/log (Math/abs %1))) nil "//"))

;;;;;;;;;;;;;;;;;;;;;PARSING;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def *constant
  (+map (fn [[a b c d]]  (Constant (read-string (str (str a) (apply str b) (str c) (apply str d)))))
        (+seq (+opt (+char "-")) (+plus *digit) (+opt (+char "."))  (+opt (+plus *digit) )) ))

(def *var (+map Variable (+str (+star *letter))))

(def *operand (+or *constant *var))

(def *pow (+str (*word "**")))
(def *log (+str (*word "//")))
(def *parseOp (+or *pow *log (+char "+-*/")))

(def *negate (+str (*word "negate")))
(def unary_list [*negate])

(def Op {"+" Add "-" Subtract "*" Multiply "/" Divide "negate" Negate "**" Pow "//" Log})


(declare *comb_parse)

(defn *object [parser] (+map (fn [a]
                               (apply (get Op (str (peek a))) (pop a))
                               ) parser))

(def *unary (*object
              (+seq *ws (+ignore (+char "("))
                    *ws (delay *comb_parse) *ws (apply +or unary_list) *ws (+ignore (+char ")")))
              ))

(def *binary (*object (+seq
                        *ws (+ignore (+char "(")) *ws
                        (delay *comb_parse) *ws (delay *comb_parse)
                        *ws *parseOp *ws (+ignore (+char ")")) *ws)))

(def *comb_parse
  (+or *unary
       *binary
       (+map (fn [[a]] a) (+seq *ws *operand *ws))
       )
  )

(def parseObjectSuffix  (+parser *comb_parse))



