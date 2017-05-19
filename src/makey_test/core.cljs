(ns makey-test.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "This text is printed from src/makey-test/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom { :keys #{"a" "b" "c"}}))

(defn pad [k]
  [:div {:key k :class "Key"}
    [:span k]])

(defn app []
  (fn []
    [:div {:class "App"}
      [:h1 "Makey Test"]
      [:div {:class "Keys"}
        (for [k (:keys @app-state)]
          (pad k))]]))

(defn onKeyDown [e]
  (let [key (.toUpperCase (.-key e))]
  (swap! app-state update-in [:keys] conj key)
  (.log js/console key)))

(def app-with-events
  (with-meta app
    {:component-did-mount
      (fn []
        (.log js/console "Mount!")
        (.addEventListener js/window "keydown" onKeyDown))
    :component-will-unmount
      (fn []
        (.log js/console "NO? Unmount!")
        (.removeEventListener js/window "keydown" onKeyDown))}))

(reagent/render-component [app-with-events]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
