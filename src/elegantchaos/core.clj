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
    :x-rotation-speed 0.05
    :y-rotation 0.0
    :y-rotation-speed 0.05
        }))

(def scalef 100.0)

(defn get-trajectory [example-name]
    (let [{f :f x0 :x0} (examples example-name)]
        (->> (iter f x0 0.01 5000)
            (map fnext))))

(defn draw []
  (background 255)
  (stroke-weight (/ 1.0 scalef))
  (translate (/ (width) 2) (/ (height) 2) 5)
  (scale scalef)
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

(defn key-pressed []
    (let [k (key-as-keyword)]
        (cond
            (= k :up) (swap! sketchstate aggregate-in :x-rotation + :x-rotation-speed)
            (= k :down) (swap! sketchstate aggregate-in :x-rotation - :x-rotation-speed)
            (= k :left) (swap! sketchstate aggregate-in :y-rotation + :y-rotation-speed)
            (= k :right) (swap! sketchstate aggregate-in :y-rotation - :y-rotation-speed))))

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
