(ns kapellmeister.file
    (:use [kapellmeister.channels :only [update-keys]]
          [clojure.core.async :only [chan put!]]))

(def HOME (System/getProperty "user.home"))
(def DIR (str HOME "/.kapellmeister/"))
(.mkdir (java.io.File. DIR))
(def KEYS_LOCATION (str DIR "keys"))
(def WELCOME_LOCATION (str DIR "welcome"))
(def DEFAULT {
        :play "control 1"
        :forward "control 8"
        :back "control 7"
    })

(def DEFAULT_KEYS
    (try (let [keys (load-file KEYS_LOCATION)]
            (put! update-keys keys)
            keys)
        (catch java.io.FileNotFoundException e
            DEFAULT)))

(def SHOULD_WELCOME 
    (try (let [times (load-file WELCOME_LOCATION)
              should-welcome (< times 2)]
            (when should-welcome
                (spit WELCOME_LOCATION (inc times)))
            should-welcome)
        (catch java.io.FileNotFoundException e
            (spit WELCOME_LOCATION 0)
            true)))

(defn save-keys! [keys]
    (put! update-keys keys)
    (spit KEYS_LOCATION keys))
