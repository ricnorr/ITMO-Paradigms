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
(def getToken #(proto-get %1 :token))
(def getFunc #(proto-get %1 :func))
(def getDiffer #(proto-get %1 :differ))

(def opProto
  {:evaluate
   (fn [obj args] (apply (getFunc obj) (mapv #(evaluate %1 args) (getVals obj))))
   :diff
   (fn [obj args] (let [vals (getVals obj)
                        diff_vals (mapv #(diff %1 args) vals)]
                    (apply (getDiffer obj) (concat vals diff_vals))
                    ))
   :toString
   (fn [obj] (str "(" (getToken obj) " " (clojure.string/join " " (mapv toString (getVals obj))) ")"))
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

(def unaryProto
  {:evaluate
   (fn [obj args] ((get obj :func) (evaluate (first (getVals obj)) args)))
   :diff
   (fn [obj args] ((getDiffer obj) (first (getVals obj)) (diff (first (getVals obj)) args)))
   :toString
   (fn [obj]  (str "(" (getToken obj) " " (clojure.string/join " " (mapv toString (getVals obj))) ")"))
   })


(def Add (constructor opProto + #(Add %3 %4) "+"))
(def Subtract (constructor opProto - #(Subtract %3 %4) "-"))
(def Multiply (constructor opProto * #(Add (Multiply %3 %2) (Multiply %4 %1)) "*"))
(def Divide (constructor opProto #(/ (double %1) (double %2)) #(Divide (Subtract (Multiply %3 %2) (Multiply %1 %4)) (Multiply %2 %2)) "/"))
(def Negate (constructor opProto #(- %1) #(Negate %2)  "negate"))
(def Constant (constructor constProto nil nil nil))
(def Variable (constructor variableProto nil nil nil))

(def Square (constructor unaryProto
                         #(Math/pow %1 2)
                         #(Multiply (Constant 2) (Multiply %1 %2))
                         "square"))

(def Sqrt (constructor unaryProto #(Math/sqrt (Math/abs %1))
                       #(Multiply
                          (Square (Sqrt %1))
                          (Multiply %2
                                    (Divide
                                      (Constant 1)
                                      (Multiply
                                        (Constant 2)
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

