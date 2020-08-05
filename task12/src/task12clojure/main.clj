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
(def toString (method :toString))
(def getVals #(proto-get %1 :vals))
(def toStringSuffix (method :toStringSuffix))
(def getToken #(proto-get %1 :token))
(def getFunc #(proto-get %1 :func))
(def getDiffer #(proto-get %1 :differ))

(def opProto
  {:evaluate
   (fn [obj args] (apply (getFunc obj) (mapv #(evaluate %1 args) (getVals obj))))
   :diff
   (fn [obj args] (let [vals (getVals obj)
                        diff_vals (mapv #(diff %1 args) vals)]
                    (comment ":NOTE: better to use separate lists insead of joined list of vals and diffs")
                    (apply (getDiffer obj) (concat vals diff_vals))
                    ))
   :toString
   (fn [obj] (str "(" (getToken obj) " " (clojure.string/join " " (mapv toString (getVals obj))) ")"))

   :toStringSuffix
   (fn [obj] (str "(" (clojure.string/join " " (mapv toStringSuffix (getVals obj))) " " (getToken obj) ")"))
   })


(declare Constant)

(def varConstProto
  {
   :evaluate
   (fn [obj args] ((getFunc obj) obj args) )
   :toString
   (fn [obj] ((get obj :token) obj))
   :diff
   (fn [obj args] ((getDiffer obj) obj args))
   :toStringSuffix
   (fn [obj] ((get obj :token) obj))
   })

;was unary proto
(def opConstructor #(constructor opProto %1 %2 %3))

(def Add (opConstructor + #(Add %3 %4) "+"))
(def Subtract (opConstructor - #(Subtract %3 %4) "-"))
(def Multiply (opConstructor * #(Add (Multiply %3 %2) (Multiply %4 %1)) "*"))
(def Divide (opConstructor #(/ (double %1) (double %2)) #(Divide (Subtract (Multiply %3 %2) (Multiply %1 %4)) (Multiply %2 %2)) "/"))
(def Negate (opConstructor #(- %1) #(Negate %2)  "negate"))

(declare ONE)
(declare ZERO)
(declare TWO)


(def Constant (constructor varConstProto
                           (fn [obj & args] (first (getVals obj)))
                           (fn [& args] ZERO)
                           (fn [obj] (format "%.1f" (double (first (getVals obj)))))))
(def ONE (Constant 1))
(def ZERO (Constant 0))
(def TWO (Constant 2))

(def Variable (constructor varConstProto
                           (fn [obj args] (get args (str (first (getVals obj))) nil))
                           (fn [obj args] (if (= args (first (getVals obj))) ONE ZERO))
                           (fn [obj] (str (first (getVals obj))))))

(def Pow (opConstructor #(Math/pow %1 %2) nil "**"))
(def Log (opConstructor #(/ (Math/log (Math/abs %2)) (Math/log (Math/abs %1))) nil "//"))

(def Square (opConstructor
              #(Math/pow %1 2)
              #(Multiply (Constant 2) (Multiply %1 %2))
              "square"))

(def Sqrt (opConstructor #(Math/sqrt (Math/abs %1))
                         #(Multiply
                            (Square (Sqrt %1))
                            (Multiply %2
                                      (Divide
                                        ONE
                                        (Multiply
                                          TWO
                                          (Multiply (Sqrt %1) %1)))))
                         "sqrt"))



(def strOper {'+ Add '- Subtract '* Multiply '/ Divide 'Negate Negate 'negate Negate 'square Square 'sqrt Sqrt})

(defn parse [expr]
  (cond
    (number? expr) (Constant expr)
    (symbol? expr) (Variable (str expr))
    (number? (first expr)) (Constant (first expr))
    (contains? strOper (first expr)) (apply (get strOper (first expr)) (mapv parse (rest expr)))
    :else (parse (first expr))))

(defn parseObject [str] (parse (read-string str)))
;;;;;;;;;;;END OF TASK11;;;;;;;;;;;;;

;;;;;;;STD LIBRARY FOR TASK12;;;;;;;;;;;;;;
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
(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))
(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))
(def *ob (+ignore (+char "(")))
(def *cb (+ignore (+char ")")))

;;;;;;;;END OF STD LIBRARY;;;;;;;;;


(def *constant
  (+map (fn [[a b c d]]  (Constant (read-string (str (str a) (apply str b) (str c) (apply str d)))))
        (+seq *ws (+opt (+char "-")) (+plus *digit) (+opt (+char "."))  (+opt (+plus *digit) )) ))

(def *pow (+str (*word "**")))
(def *log (+str (*word "//")))
(def *negate (+str (*word "negate")))
(def *parseOp (+or *negate *pow *log (+char "+-*/")))

(def *var (+map Variable (+str (+plus (+char "xyz")))))

(def *operand (+or *constant *var))



(def Op {"+" Add "-" Subtract "*" Multiply "/" Divide "negate" Negate "**" Pow "//" Log})


(declare *comb_parse)

(defn *object [parser] (+map (fn [a]
                               (println a)
                               (apply (get Op (str (peek a))) (first (pop a)))
                               ) parser))

(comment ":NOTE: `*unary` and `*binary` looks like copy-pasted")




(def ary (+or (*object (+seq
                         *ws (+ignore (+char "(")) *ws
                         (+plus (delay ary))
                         *ws *parseOp *ws (+ignore (+char ")")) *ws))
              (+map (fn [[a]] (println "parsed") a) (+seq *ws *operand *ws))))



(def parseObjectSuffix  (+parser ary))

(println (toStringSuffix (parseObjectSuffix "(x negate)")))


