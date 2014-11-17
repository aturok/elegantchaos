(ns elegantchaos.examples.chapter2)

(defn po4-xdot [t [x v z]] v)
(defn po4-vdot [t [x v z]] (* (- (Math/sin z) 1.2) x x x))
(defn po4-zdot [t [x v z]] 1.0)

(def po4 [po4-xdot po4-vdot po4-zdot])