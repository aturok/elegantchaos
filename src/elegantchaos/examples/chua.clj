(ns elegantchaos.examples.chua)

(def c1 15.6)
(def c2 1.0)
(def c3 28.0)
(def m0 -1.143)
(def m1 -0.714)

(defn fnl [x] (+ (* m1 x) (* 0.5 (- m0 m1) (- (Math/abs (+ x 1)) (Math/abs (- x 1))))))
(defn f1 [t [x y z]] (* c1 (- y x (fnl x))))
(defn f2 [t [x y z]] (* c2 (+ x (- y) z)))
(defn f3 [t [x y z]] (- (* c3 y)))

(def equations [f1 f2 f3])
