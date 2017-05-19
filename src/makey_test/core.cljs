(ns makey-test.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)
(defonce ac (js/AudioContext.))

(defn toUrl [name]
  (str "https://antropoloops.github.io/looper/mp3/" name))

(def loops [
  {:name "A1_1" :path "A1_1.mp3" :group "Voices" :key "Q"}
  {:name "A1_2" :path "A1_2.mp3" :group "Voices" :key "Q"}
])

(defonce app-state (atom {
  :keys #{}
  :buffers {}}))

(defn updateKeys [fn k]
  (swap! app-state update-in [:keys] fn k))

(defn addBuffer [name buffer]
  (.log js/console "BUFFER" name buffer)
  (swap! app-state update-in [:buffers] assoc name buffer))

(defn pad [k]
  [:div {:key k :class "Key"}
    [:span k]])

(defn bufferSource [buffer]
  (let [source (.createBufferSource ac)]
    (aset source "buffer" buffer)
    source))

(defn play [buffer]
  (doto (bufferSource buffer)
    (.connect (.-destination ac))
    (.start)))

(defn loop-view [name buffer]
  [:a {
    :key name :class "Loop"
    :href "#!" :on-click #(play buffer)}
    name])

(defn app []
  (fn []
    [:div {:class "App"}
      [:h1 "Makey Test"]
      [:div {:class "Loops"}
        (for [[name buffer] (:buffers @app-state)]
          (loop-view name buffer))]
      [:div {:class "Keys"}
        (for [k (:keys @app-state)]
          (pad k))]]))

(defn keyOf [e]
  (.toUpperCase (.-key e)))

(defn onKeyDown [e]
  (updateKeys conj (keyOf e)))

(defn onKeyUp [e]
  (updateKeys disj (keyOf e)))

(defn toArrayBuffer [response]
  (.arrayBuffer response))

(defn decodeBuffer [buffer]
  (.decodeAudioData ac buffer))

(defn fetch [url]
  (.fetch js/window url))

(defn fetchAllSounds []
  (doseq [loop loops]
  (-> (:path loop)
    toUrl
    fetch
    (.then toArrayBuffer)
    (.then decodeBuffer)
    (.then (partial addBuffer (:name loop)))
    )))

(def app-with-events
  (with-meta app
    {:component-did-mount
      (fn []
        (fetchAllSounds)
        (.addEventListener js/window "keydown" onKeyDown)
        (.addEventListener js/window "keyup" onKeyUp))
    :component-will-unmount
      (fn []
        (.removeEventListener js/window "keydown" onKeyDown)
        (.removeEventListener js/window "keyup" onKeyUp))}))

(reagent/render-component [app-with-events]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
