(ns elegantchaos.math-test
  (:require [clojure.test :refer :all]
            [elegantchaos.math :refer :all]))

(defn ivp [t [y]] (- 1 (* t y)))

(defn almost= [v1 v2]
	(every? (fn [[a b]] (<= (Math/abs (- a b)) 0.0001))
		(map vector v1 v2)))

(deftest rk4-coefficients-checks
  (testing "y' = 1 - t*y at 1.0 with y(0) = 1.0"
    (is (almost= (map first (rk4-coeffs (juxt ivp) [1.0] 0.0 1.0)) [1.0 0.25 0.4375 -0.4375])))
  (testing "y' = 1 - t*y at 1.0 with y(0) = 1.0"
    (is (almost= (map first (rk4-coeffs (juxt ivp) [1.0] 0.0 0.5)) [1.0 0.6875 0.70703125 0.3232421875]))))
