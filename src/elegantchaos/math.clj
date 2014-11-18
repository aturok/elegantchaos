(ns elegantchaos.math)

(defn addvec [& vs]
	(map (partial reduce +)
		(apply (partial map vector) vs)))

(defn timesvec [k v]
	(map (partial * k) v))

(defn rk4-coeffs [f x t dt]
	(let [hdt (* 0.5 dt)
		  k1 (f t x)
		  k2 (f (+ t hdt) (addvec x (map (partial * hdt) k1)))
		  k3 (f (+ t hdt) (addvec x (map (partial * hdt) k2)))
		  k4 (f (+ t  dt) (addvec x (map (partial *  dt) k3)))]
		[k1 k2 k3 k4]))

(defn rk4-samestep [fdots dt]
	(let [f (apply juxt fdots)]
		(fn [[t x0]]
			(let [[k1 k2 k3 k4] (rk4-coeffs f x0 t dt)]
				[(+ t dt)
				 (addvec
				 	x0
				 	(timesvec
				 		(* dt (/ 1.0 6.0))
				 		(addvec
				 			k1
				 			(timesvec 2.0 k2)
				 			(timesvec 2.0 k3)
				 			k4)))]))))

(defn iter
	([f x0 dt n]
		(iter f x0 0.0 dt n))
	([f x0 t0 dt n]
		(let [rk (rk4-samestep f dt)]
			(take n (iterate rk [t0 x0])))))

