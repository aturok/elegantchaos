(ns elegantchaos.core
    (:use [elegantchaos.math])
    (:require [quil.core :refer :all]
              [elegantchaos.examples.chua :as chua]
              [elegantchaos.examples.chapter2 :as chapter2]
              [quil.helpers.drawing :refer [line-join-points]])
    (:gen-class))

(def examples {
    "chua" {:f chua/equations :x0 [0.7 0.0 0.0]}
    "chapter2-po4" {:f chapter2/po4 :x0 [5.0 0.0 0.0]}
    })

(defn setup []
  (background 255)
  (stroke 00))

(def sketchstate (atom {
    :x-rotation 0.0
    :y-rotation 0.0
    :z-rotation 0.0
    :rotation-speed 0.05
    :scale-factor 100.0
    :scale-step 5.0
        }))

(defn get-trajectory [example-name]
    (let [{f :f x0 :x0} (examples example-name)]
        (->> (iter f x0 0.01 5000)
            (map fnext))))

(defn draw []
  (background 255)
  (stroke-weight (/ 1.0 (:scale-factor @sketchstate)))
  (translate (/ (width) 2) (/ (height) 2) 5)
  (scale (:scale-factor @sketchstate))
  (rotate-z (:z-rotation @sketchstate))
  (rotate-y (:y-rotation @sketchstate))
  (rotate-x (:x-rotation @sketchstate))
  (dorun
    (map #(apply line %) (line-join-points (:trajectory @sketchstate)))))

(defn add-key [state keycode]
    (assoc state :pressed-keys (conj (:pressed-keys state) keycode)))

(defn remove-key [state keycode]
    (assoc state :pressed-keys (disj (:pressed-keys state) keycode)))

(defn aggregate-in [state result-key op delta-key]
    (assoc state result-key
        (op (state result-key) (state delta-key))))

(defn rotate-sketch [axis direction]
    (swap! sketchstate aggregate-in axis direction :rotation-speed))

(defn key-pressed []
    (let [k (key-as-keyword)]
        (cond
            (= k :up) (rotate-sketch :x-rotation +)
            (= k :down) (rotate-sketch :x-rotation -)
            (= k :left) (rotate-sketch :y-rotation +)
            (= k :right) (rotate-sketch :y-rotation -)
            (= k :') (rotate-sketch :z-rotation +)
            (= k (keyword "/")) (rotate-sketch :z-rotation -)
            (= k :-) (swap! sketchstate aggregate-in :scale-factor - :scale-step)
            (= k :=) (swap! sketchstate aggregate-in :scale-factor + :scale-step))))

(defsketch trajectory
  :title "Chua"
  :setup setup
  :draw draw
  :key-pressed key-pressed
  :size [1000 600]
  :renderer :opengl)

(defn -main
  "I launch sketch"
  [example & other-args]
  (swap! sketchstate assoc :trajectory (get-trajectory example)))
