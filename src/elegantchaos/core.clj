(ns elegantchaos.core
    (:use [elegantchaos.math])
    (:require [quil.core :refer :all]
              [elegantchaos.examples.chua :as chua]
              [elegantchaos.examples.chapter2 :as chapter2]
              [elegantchaos.examples.chapter3 :as chapter3]
              [quil.helpers.drawing :refer [line-join-points]])
    (:gen-class))

(def examples {
    "chua" {:f chua/equations :x0 [0.7 0.0 0.0]}
    "chapter2-po4" {:f chapter2/po4 :x0 [5.0 0.0 0.0]}
    "moore-spiegel" {:f chapter3/moore-spiegel :x0 [4.0 7.0 2.0]}
    "chapter3-sqm" {:f chapter3/sqm :x0 [1.0 -0.8 0.0]}
    "halvorsen" {:f chapter3/halvorsen :x0 [-6.4 0.0 0.0]}
    })

(defn setup []
  (background 255)
  (stroke 00))

(def sketchstate (atom {
    :x-rotation 0.0
    :y-rotation 0.0
    :z-rotation 0.0
    :rotation-speed 0.05
    :z-position 5.0
    :z-step 10.0
    :scale-factor 100.0
    :scale-step 5.0
        }))

(defn get-trajectory [example-name]
    (let [{f :f x0 :x0} (examples example-name)]
        (->> (iter f x0 0.01 5000)
            (map fnext))))

(defn draw []
    (let [{
        scale-factor :scale-factor
        x-rotation :x-rotation
        y-rotation :y-rotation
        z-rotation :z-rotation
        z-position :z-position
        trajectory :trajectory
        } @sketchstate]

        (background 255)
        (stroke-weight (/ 1.0 scale-factor))
        (translate (/ (width) 2) (/ (height) 2) z-position)
        (scale scale-factor)
        (rotate-z z-rotation)
        (rotate-y y-rotation)
        (rotate-x x-rotation)
        (dorun
            (map #(apply line %) (line-join-points trajectory)))))

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
            (= k :w) (swap! sketchstate aggregate-in :z-position + :z-step)
            (= k :s) (swap! sketchstate aggregate-in :z-position - :z-step)
            (= k :-) (swap! sketchstate aggregate-in :scale-factor - :scale-step)
            (= k :=) (swap! sketchstate aggregate-in :scale-factor + :scale-step))))

(defn run-sketch [title]
  (sketch
    :title title
    :setup setup
    :draw draw
    :key-pressed key-pressed
    :size :fullscreen
    :renderer :opengl))

(defn -main
  "I launch sketch"
  [example & other-args]
  (if (= example "list")
    (doseq [e (sort (keys examples))]
      (println e))
    (do
      (swap! sketchstate assoc :trajectory (get-trajectory example))
      (run-sketch example))))
