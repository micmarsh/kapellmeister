(ns mediakeys.hotkeys
    (:use [mediakeys.file :only [DEFAULT_KEYS]]
           mediakeys.rx))

(import [com.tulskiy.keymaster.common Provider HotKeyListener])
(import [javax.swing KeyStroke])

; (defn ^HotKeyListener action-listener [action]
;    (proxy [HotKeyListener] []
;             (onHotKey [event] (action event))))

(defn make-keystroke [keystroke]
    (KeyStroke/getKeyStroke keystroke))

(def current-keys (atom DEFAULT_KEYS))

(defn register-keys! [provider keys]
    (observable 
        (fn [s]
            (doseq [[action hotkey] keys
                    n [(println hotkey)]
                    keystroke [(make-keystroke hotkey)]]
                (.register provider keystroke
                    (proxy [HotKeyListener] []
                        (onHotKey [event]
                            (next! s action))))))))

(def prstr (comp println str))

(def provider (atom (Provider/getCurrentProvider false)))

(defn keypress-events! [key-changes]
    (mapcat key-changes 
        (fn [key-update] 
            (let [new-keys (swap! current-keys #(merge % key-update))
                  new-provider (Provider/getCurrentProvider false)
                  result (register-keys! new-provider new-keys)]
                (.reset @provider)
                (.stop @provider)
                (reset! provider)
                result))))
