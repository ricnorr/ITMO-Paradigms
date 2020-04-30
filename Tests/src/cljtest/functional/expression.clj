(defn makeOperation [func]
  (fn [& args1] (fn [args2] (apply func (mapv #(%1 args2) args1)))))

(def add (makeOperation +))
(defn constant [val] (fn [& args] val))
(defn variable [name] #(get %1 name 0))
(def subtract (makeOperation -))
(def multiply (makeOperation *))
(def divide (makeOperation #(/ (double %1) (double %2))))
(def negate (makeOperation #(- %1)))

(def pw (makeOperation #(Math/pow %1 %2)))

(def lg (makeOperation #(/ (Math/log (Math/abs %2)) (Math/log (Math/abs %1)))))

(def strOper {'+ add '- subtract '* multiply '/ divide 'negate negate 'pw pw 'lg lg})

(defn parse [expr]
  (cond
    (number? expr) (constant expr)
    (symbol? expr) (variable (str expr))
    (contains? strOper (first expr)) (apply (get strOper (first expr)) (mapv parse (rest expr)))
    :else (parse (first expr))))

(defn parseFunction [str] (parse (read-string str)))
