(defn vectorOp [func] (fn [& args] (apply mapv func args)))

(def v+ (vectorOp +))

(def v- (vectorOp -))

(def v* (vectorOp *))

(def vd (vectorOp /))

(defn scalar [a b] (reduce + ((vectorOp *) a b)))

(defn v*s [v s] ((vectorOp (partial * s)) v))

(def m+ (vectorOp v+))

(def m- (vectorOp v-))

(def m* (vectorOp v*))

(def md (vectorOp vd))

(defn m*s [a b] (mapv (vectorOp (partial * b)) a))

(defn transpose [a] (apply mapv vector a))

(defn m*v [a b] transpose (mapv (partial scalar b) a))

(defn m*m [a b] (let [transB (transpose b)] (mapv #(mapv (partial scalar %1) transB) a)))

(defn vect [a b]
  (letfn [(det [i, j] (- (* (get a i) (get b j))(* (get a j) (get b i))))]
                    [(det 1 2) (- (det 0 2)) (det 0 1)]))

(defn shapelessCarcas [func a b]
  (if (and (number? a) (number? b)) (func a b) (mapv (partial shapelessCarcas func) a b)))

(defn s+ [a b] (shapelessCarcas + a b))
(defn s- [a b] (shapelessCarcas - a b))
(defn s* [a b] (shapelessCarcas * a b))

