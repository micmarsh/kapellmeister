
buttons = { }

buttonExists = (name) ->
    button = buttons[name]
    button and button.length # b/c jQuery

handlePlayPause = do ->
    playing = true #ideally, you'll want something more correct than this
    # may be hard to standardize that across all of these diff things, tho
    (fn) ->
        (button) ->
            if playing is true and button is 'play'
                button = 'pause'
                playing = false
            else
                playing = true
            fn button

press = handlePlayPause (button) ->
    unless buttonExists button
        buttons[button] = $ buttonElements[button]
    buttons[button].click()

connection = new WebSocket('ws://localhost:8886/controls')
connection.onopen = -> console.log 'connected to controls'
connection.onmessage = ({data}) -> press data

setTimeout ->
    injectScript('http://code.jquery.com/jquery-2.1.0.min.js') unless $?
, 500

