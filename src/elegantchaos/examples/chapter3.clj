(ns elegantchaos.examples.chapter3)

;x''' = -x'' + 9x' - x^2 x' - 5x
(defn ms-xdot [t [x xd xdd]] xd)
(defn ms-xddot [t [x xd xdd]] xdd)
(defn ms-xdddot [t [x xd xdd]] (+ (- xdd) (* 9 xd) (- (* x x xd)) (- (* 5 x))))

(def moore-spiegel [ms-xdot ms-xddot ms-xdddot])

(def sqm [
	(fn [t [x y z]] (- z))
	(fn [t [x y z]] (+ (- (* x x)) (- y)))
	(fn [t [x y z]] (+ 1.7 (* 1.7 x) y))
	])

(defn halvorsen-f [a]
	(fn [t [x y z]] (+ (- (* a x)) (- (* 4 y)) (- (* 4 z)) (- (* y y)))))

(def halvorsen
	(let [f (halvorsen-f 1.3)]
		[f
		 (fn [t [x y z]] (f t [y z x]))
		 (fn [t [x y z]] (f t [z x y]))
		 ]))
