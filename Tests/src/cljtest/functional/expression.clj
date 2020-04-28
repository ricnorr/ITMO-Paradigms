(defn makeOperation [func]
  (fn [& args1]  (fn [args2] (reduce func (mapv #(%1 args2) args1)))))

(def add (makeOperation +))
(defn constant [val] (fn [& args] val))
(defn variable [name] (fn [args] (get args name)))
(def subtract (makeOperation -))
(def multiply (makeOperation *))
(def divide (makeOperation #(/ (double %1) (double %2))))
(def negate (makeOperation -))
(def min (makeOperation #(Math/min %1 %2)))
(def max (makeOperation  #(Math/max %1 %2)))

(defn parse [expr]
  (let [strOper {'+ add '- subtract '* multiply '/ divide 'negate negate}]
    (cond
      (number? expr) (constant expr)
      (symbol? expr) (variable (str expr))
      (contains? strOper (first expr)) (apply (get strOper (first expr)) (mapv parse (rest expr)))
      (number? (first expr)) (constant (first expr))
      :else (parse (first expr)))))

(defn parseFunction [str] (parse (read-string str)))



