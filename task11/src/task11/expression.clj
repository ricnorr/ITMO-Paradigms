(defn proto-get [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :proto) (proto-get (obj :proto) key)))

(defn proto-call [obj key]
  (proto-get obj key))

(defn constructor [func proto differ wrap token]
  (fn [& vals] {:proto proto :vals (apply vector vals) :func func :differ differ :wrap wrap :token token}))

(defn method [key] (fn [obj & args] (apply (proto-call obj key) obj args)))

(def evaluate (method :evaluate))
(def diff (method :diff))
(def toString (method :toString))

(defn diffRule [wrap func a b args]
  (if (= (count a) 1)
    (func (first a) b args)
    (func (apply wrap a) b args)))


(def opProto
  {:evaluate
   (fn [obj args] (apply (get obj :func) (mapv #(evaluate %1 args) (get obj :vals))))
   :diff
   (fn [obj args] (let [vals (get obj :vals)]
                    (diffRule (get obj :wrap) (get obj :differ) (pop vals) (peek vals) args)
                    ))
   :toString
   (fn [obj] (str "(" (get obj :token) " " (clojure.string/join " " (mapv toString (get obj :vals))) ")")) ; & obj
  })


(def Add (constructor + opProto #(Add (diff %1 %3) (diff %2 %3)) Add "Add"))
(def Subtract (constructor - opProto #(Subtract (diff %1 %3) (diff %2 %3)) Subtract "Subtract"))
(def Multiply (constructor * opProto #(Add (Multiply (diff %1 %3) %2) (Multiply %1 (diff %2 %3))) Multiply "Multiply"))
(def Divide (constructor / opProto #(Divide (Subtract (Multiply (diff %1 %3) %2) (Multiply %1 (diff %2 %3))) (Multiply %2 %2)) Divide "Divide"))

(def constProto
  {:evaluate
   (fn [obj args] (get obj :vals))
   :toString
   (fn [& args] (str "(Constant " (get (first args):vals) ")"))
   })

(def Constant (fn [val] {:vals val :proto (assoc constProto :diff (fn [& args] (Constant 0)))}))


(def variableProto
  {:evaluate
   (fn [obj args] (get args (str (get obj :vals)) nil))
   :diff
   (fn [obj args] (if (= args (get obj :vals)) (Constant 1) (Constant 0)))
   :toString
   (fn [& args] (str "(Variable \"" (get (first args) :vals)"\")"))
   }
  )

(def Variable (fn [name] {:vals name :proto variableProto}))

;(def exp1 (Add (Multiply (Constant 1) (Variable "x")) (Subtract (Multiply (Variable "x") (Variable "x")) (Variable "x"))))
;(println (toString exp1))
;(println (evaluate exp1 {"x" 11}))
;(println (evaluate (diff exp1 "x") {"x" 7}))

(def exp2 (Divide (Add (Multiply (Variable "x") (Variable "x")) (Multiply (Constant 7) (Variable "x")))
                  (Multiply (Constant 13) (Variable "x"))))
;;(def exp2 (Divide (Multiply (Variable "x") (Variable "x")) (Constant 10)))
(println (evaluate (diff exp2 "x") {"x" 1}))
;(println (toString (diff exp2 "x")))


;(println (type {"x" 1}))

;(println (evaluate (Variable "x") {"x" 1 "y" 2 "z" 3}))
;(println (evaluate exp2 {"x" 1 "y" 10}))
;(println (evaluate exp3 {"x" 5 "y" 14}))
;(println (evaluate exp1 {"x" 10 "y" 20}))

