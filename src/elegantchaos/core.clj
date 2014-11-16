(ns elegantchaos.core
    (:use [elegantchaos.math])
    (:require [quil.core :refer :all]
              [elegantchaos.examples.chua :as chua]
              [quil.helpers.drawing :refer [line-join-points]])
    (:gen-class))

(defn setup []
  (background 255)
  (stroke 00))

(defn draw []
  (background 255)
  (translate (/ (width) 2) (/ (height) 2) 0)
  (let [traj-with-time (iter chua/equations [0.7 0.0 0.0] 0.01 100)
        traj (map fnext traj-with-time)]
    (dorun
     (map #(apply line %) (line-join-points traj)))))

(defsketch trajectory
  :title "Chua"
  :setup setup
  :draw draw
  :size [1000 600]
  :renderer :opengl)

(defn -main
  "I launch sketch"
  [& args])
