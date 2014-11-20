(ns elegantchaos.math-test
  (:require [clojure.test :refer :all]
            [elegantchaos.math :refer :all]))

(defn ivp [t [y]] (- 1 (* t y)))

(defn almost= [a b]
	(<= (Math/abs (- a b)) 0.0001))

(defn valmost= [v1 v2]
	(every? (partial apply almost=)
		(map vector v1 v2)))

(deftest rk4-coefficients-checks
  (testing "y' = 1 - t*y at 1.0 with y(0) = 1.0"
    (is (valmost= (map first (rk4-coeffs (juxt ivp) [1.0] 0.0 1.0)) [1.0 0.25 0.4375 -0.4375])))
  (testing "y' = 1 - t*y at 1.0 with y(0) = 1.0"
    (is (valmost= (map first (rk4-coeffs (juxt ivp) [1.0] 0.0 0.5)) [1.0 0.6875 0.70703125 0.3232421875]))))

(deftest rk4-test
  (testing "y' = 1 - t*y at 1.0 with y(0) = 1.0"
    (is (valmost= (get-point (fnext (iter [ivp] [1.0] 1.0 2))) [1.3229166667])))
  (testing "y' = 1 - t*y at 1.0 with y(0) = 1.0"
    (is (valmost= (get-point (fnext (iter [ivp] [1.0] 0.5 2))) [1.342692057]))))

(deftest rk4-same-results-from-different-times
	(testing "y' = 1 - t*y at 2.0 with y(0) = 1.0 or with y(1) = rk4(y(0)) (dt = 1.0)"
		(let [
			steps 5
			y0 1.0
			dt 1.0
			traj-from-zero (iter [ivp] [y0] dt (inc steps))
			at-one (get-point (nth traj-from-zero 1))
			traj-from-one (iter [ivp] at-one 1.0 dt steps)
			points-from-zero (map get-point traj-from-zero)
			points-from-one (map get-point traj-from-one)
			]
			(is	(= (count points-from-one) (dec (count points-from-zero))))
			(is (every? (partial apply valmost=)
					(map vector points-from-one (drop 1 points-from-zero)))))))
