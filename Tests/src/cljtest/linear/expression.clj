(defn makeOperation [func] (fn [& args]  (fn [args2] (apply func (mapv (fn [x] (x args2)) args)))))

(def add (makeOperation +))
(defn constant [val] (fn [& args] val))
(defn variable [name] (fn [args] (get args name)))
(def subtract (makeOperation -))
(def multiply (makeOperation *))
(def divide (makeOperation (fn [a b] (/ (double a) (double b)))))
(def negate (makeOperation -))


(defn parse [expr]
  (let [strOper {'+ add '- subtract '* multiply '/ divide 'negate negate}]
  (cond
    (number? expr) (constant expr)
    (symbol? expr) (variable (str expr))
    (contains? strOper (first expr)) (apply (get strOper (first expr)) (mapv parse (rest expr)))
    (number? (first expr)) (constant (first expr))
    :else (parse (first expr)))))

(defn parseFunction [str] (parse (read-string str)))



