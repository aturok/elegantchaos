(ns elegantchaos.examples.chapter3)

;x''' = -x'' + 9x' - x^2 x' - 5x
(defn ms-xdot [t [x xd xdd]] xd)
(defn ms-xddot [t [x xd xdd]] xdd)
(defn ms-xdddot [t [x xd xdd]] (+ (- xdd) (* 9 xd) (- (* x x xd)) (- (* 5 x))))

(def moore-spiegel [ms-xdot ms-xddot ms-xdddot])