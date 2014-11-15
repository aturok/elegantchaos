(ns elegantchaos.math)

(defn addvec [& vs]
	(map (partial reduce +)
		(apply (partial map vector) vs)))

(defn timesvec [k v]
	(map (partial * k) v))

(defn rk4-samestep [fdots dt]
	(let [f (apply juxt fdots)]
		(fn [[t x0]]
			(let [hdt (* 0.5 dt)
				  k1 (f t x0)
				  k2 (f (+ t hdt) (addvec x0 (map (partial * hdt) k1)))
				  k3 (f (+ t hdt) (addvec x0 (map (partial * hdt) k2)))
				  k4 (f (+ t  dt) (addvec x0 (map (partial *  dt) k3)))]
				[(+ t dt)
				 (addvec
				 	x0
				 	(timesvec
				 		(/ 1.0 6.0)
				 		(addvec
				 			k1
				 			(timesvec 0.5 k2)
				 			(timesvec 0.5 k3)
				 			k4)))]))))

(defn iter [f x0 dt n]
	(let [rk (rk4-samestep f dt)]
		(take n (iterate rk [0 x0]))))

