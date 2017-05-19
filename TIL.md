# Things I learn

#### Create

`lein new figwheel makey-test -- --reagent`

#### Run

Run the `dev/user.clj`

```
Refresh Warning: IllegalArgumentException No such namespace: clojure.tools.namespace.repl  clojure.lang.Var.find (Var.java:141)
Refresh Warning: IllegalArgumentException No such namespace: clojure.tools.namespace.repl  clojure.lang.Var.find (Var.java:141)
```

#### Add event handler

`(.addEventListener js/window "keydown" onKeyDown)`

#### Reagent: run things when creating

```clojure
(defn component []
  (fn []
    (.sideEffect1 js/object)
    (.sideEffect2 js/object)
    [:div "My component"]))
```

NOTE: **it doesn't work with event handlers**

#### componentWillMount and componentWillUnmount

```clojure
(def app-with-events
  (with-meta app
    {:component-did-mount
      (fn []
        (.log js/console "Mount!")
        (.addEventListener js/window "keydown" onKeyDown))
    :component-will-unmount
      (fn []
        (.log js/console "Unmount!")
        (.removeEventListener js/window "keydown" onKeyDown))}))
```

#### -> y ->>

#### doto

#### do implícito

El `let`, el `when`, el `defn` (al menos estos)
