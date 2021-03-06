(defn makeOperation [func]
  (fn [& args1] (fn [args2] (apply func (mapv #(%1 args2) args1)))))

(def add (makeOperation +))
(defn constant [val] (fn [& args] val))
(defn variable [name] (fn [args] (get args name)))
(def subtract (makeOperation -))
(def multiply (makeOperation *))
(def divide (makeOperation #(/ (double %1) (double %2))))
(def negate (makeOperation -))

(defn mins [& args] (reduce #(Math/min %1 %2) args))
(defn maxs [& args] (reduce #(Math/max %1 %2) args))

(def min (makeOperation mins))
(def max (makeOperation  maxs))

(def strOper {'+ add '- subtract '* multiply '/ divide 'negate negate 'min min 'max max})

(defn parse [expr]
    (cond
      (number? expr) (constant expr)
      (symbol? expr) (variable (str expr))
      (number? (first expr)) (constant (first expr))
      (contains? strOper (first expr)) (apply (get strOper (first expr)) (mapv parse (rest expr)))
      :else (parse (first expr))))

(defn parseFunction [str] (parse (read-string str)))
