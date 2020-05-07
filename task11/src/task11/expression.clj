(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :proto) (proto-get (obj :proto) key)))

(defn proto-call [obj key]
  (proto-get obj key))

(def opProto)

(defn constructor [func differ token]
  (fn [& vals] {:proto opProto :vals vals :func func :differ differ :token token}))

(defn method [key] (fn [obj & args] (apply (proto-call obj key) obj args)))

(def evaluate (method :evaluate))
(def diff (method :diff))
(def toString (method :toString))
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
   (fn [obj] (str "(" (getToken obj) " " (clojure.string/join " " (mapv toString (getVals obj))) ")"))
   })


(def Add (constructor + #(Add %3 %4) "+"))
(def Subtract (constructor - #(Subtract %3 %4) "-"))
(def Multiply (constructor * #(Add (Multiply %3 %2) (Multiply %4 %1)) "*"))
(def Divide (constructor #(/ (double %1) (double %2)) #(Divide (Subtract (Multiply %3 %2) (Multiply %1 %4)) (Multiply %2 %2)) "/"))
(def Negate (constructor #(- %1) #(Negate %2)  "negate"))

(def Constant)

(def constProto
  {:evaluate
   (fn [obj & args] (get obj :vals))
   :toString
   (fn [obj] (format "%.1f" (double (getVals obj))))
   :diff
   (fn [& args] (Constant 0))
   })


(def Constant (fn [val] {:vals val :proto constProto}))


(def variableProto
  {:evaluate
   (fn [obj args] (get args (str (getVals obj)) nil))
   :diff
   (fn [obj args] (if (= args (getVals obj)) (Constant 1) (Constant 0)))
   :toString
   (fn [obj] (str (getVals obj)))
   })

(def Variable (fn [name] {:vals name :proto variableProto}))


(def strOper {'+ Add '- Subtract '* Multiply '/ Divide 'Negate Negate 'negate Negate})

(defn parse [expr]
  (cond
    (number? expr) (Constant expr)
    (symbol? expr) (Variable (str expr))
    (number? (first expr)) (Constant (first expr))
    (contains? strOper (first expr)) (apply (get strOper (first expr)) (mapv parse (rest expr)))
    :else (parse (first expr))))

(defn parseObject [str] (parse (read-string str)))

(def exp (Constant 10))
(println (evaluate exp {"x" 1}))