/**
 * Created by bilbowm on 20/10/2015.
 */
var output;
var uri;
var wss = false;
var socketLibs = {
    socketIo : 'socket.io',
    sockJs : 'SockJS'
}
function useLibrary(lib) {
    var opt = $('select#socketLibrary option:selected').text();
    return lib != null ? opt === lib : opt;
}
function setConnected(connected) {
    if (connected == true) {
        $('input#customSocket').attr('disabled','disabled');
        $('#wss').attr('disabled','disabled');
        $('select#toSocket').attr('disabled','disabled');
        $('div#openSocket').attr('disabled','disabled');
        $('div#closeSocket').removeAttr('disabled');
        $('div#sendButton').removeAttr('disabled');
        $('select#socketLibrary').attr('disabled','disabled');
        console.log('CONNECTED >>> ');
    } else {
        $('input#customSocket').removeAttr('disabled');
        $('#wss').removeAttr('disabled');
        $('select#toSocket').removeAttr('disabled');
        $('div#openSocket').removeAttr('disabled');
        $('div#closeSocket').attr('disabled','dissabled');
        $('div#sendButton').attr('disabled','disabled');
        $('select#socketLibrary').removeAttr('disabled');
        console.log('DISCONNECTED <<<');
    }
    updateUri();
    updateWss();
}

function getMessage() {
    return $('textarea#sendMessage').val();
}

function wsUri() {
    if ($('input#customSocket').prop('disabled'))
        return (wss ? "wss://" : "ws://") + uri;
    else
        return uri;
};

function init() {
    setConnected(false);
    console.log("uri: '" + wsUri() + "'");
    output = document.getElementById("output");
}

function updateUri() {
    uri = $('select#toSocket  option:selected').text();
    if (uri.indexOf('--custom--') > -1) {
        uri = $('input#customSocket').val();
        $('#wss').attr('disabled','disabled');
        $('input#customSocket').removeAttr('disabled');//.prop('disabled', false);//.disabled(false);
    } else {
        $('#wss').removeAttr('disabled');
        $('input#customSocket').attr('disabled', 'disabled');
    }
    console.log("uri: '" + wsUri() + "'");
}

function updateWss() {
    wss = $('#wss').prop('checked');
    console.log("uri: '" + wsUri() + "'");
}

function sendMessage() {
    var message = getMessage();
    console.log("sending: '" + message + "', to: " + wsUri());
    if (useLibrary("socket.io")) {
        if (window.websocket) {
            writeToScreen("SENT: " + message);
            websocket.send(message);
        } else {
            console.log("NO CONNECTION!");
        }
    } else if (useLibrary("SockJS")) {
        writeErrToScreen("SockJS Not implemented");
    }
    //testWebSocket();
}

function disconnect() {
    if (window.websocket)
        websocket.close();
}
function connect() {
    switch (useLibrary()) {
        case socketLibs.sockJs:
            writeErrToScreen("SockJS Not implemented");
            return;
        case socketLibs.socketIo:
            if (window.websocket)
                websocket.close();
            try {
                window.websocket = new WebSocket(wsUri());
                websocket.onopen = function (evt) {
                    onOpen(evt);
                    setConnected(true);
                };
                websocket.onclose = function (evt) {
                    onClose(evt)
                    setConnected(false);
                };
                websocket.onmessage = function (evt) {
                    onMessage(evt)
                };
                websocket.onerror = function (evt) {
                    onError(evt);

                };
            } catch (e) {
                writeErrToScreen(e);
            }
    }
}


function onOpen(evt) {
    writeToScreen("CONNECTED");
    //doSend(message);
}
function onClose(evt) {
    writeToScreen("DISCONNECTED");
}
function onMessage(evt) {
    writeToScreen('<span style="color: rgba(0, 255, 248, 1);">RESPONSE: ' + evt.data + '</span>');
    //websocket.close();
}
function onError(evt) {
    writeToScreen('<span style="color: rgba(255, 170, 167, 1);">ERROR:</span> ' + evt.data);
}

function writeErrToScreen(err) {
    writeToScreen('<span style="color: rgba(255, 170, 167, 1);">ERROR:</span> ' + err);
}

function writeToScreen(message) {

    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    output.appendChild(pre);

    try {
        output.scrollTop = output.scrollHeight;
    } catch (e){
        console.log(e);
    }
}


window.addEventListener("load", init, false);
