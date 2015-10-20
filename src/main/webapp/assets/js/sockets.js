/**
 * Created by bilbowm on 20/10/2015.
 */
var output;
var uri;
var wss = false;

var getMessage = function() {
    return $('textarea#sendMessage').html();
};

var wsUri = function () {
    return (wss ? "wss://" : "ws://") + uri;
};

function init() {
    updateUri();
    updateWss();
    console.log("uri: '" + wsUri() + "'");
    output = document.getElementById("output");
}

function updateUri() {
    uri = $('select#toSocket  option:selected').text();
    console.log("uri: '" + wsUri() + "'");
}

function updateWss() {
    wss = $('#wss').prop('checked');
    console.log("uri: '" + wsUri() + "'");
}

function sendMessage() {
    var message = getMessage();
    console.log("sending: '" + message + "', to: " + wsUri());

    if (window.websocket) {
        writeToScreen("SENT: " + message);
        websocket.send(message);
    } else {
        console.log("NO CONNECTION!");
    }
    //testWebSocket();
}

function disconnect() {
    if (window.websocket)
        websocket.close();
}
function connect() {
    if (window.websocket)
        websocket.close();
    websocket = new WebSocket(wsUri());
    websocket.onopen = function (evt) {
        onOpen(evt)
    };
    websocket.onclose = function (evt) {
        onClose(evt)
    };
    websocket.onmessage = function (evt) {
        onMessage(evt)
    };
    websocket.onerror = function (evt) {
        onError(evt)
    };
}


function onOpen(evt) {
    writeToScreen("CONNECTED");
    //doSend(message);
}
function onClose(evt) {
    writeToScreen("DISCONNECTED");
}
function onMessage(evt) {
    writeToScreen('<span style="color: blue;">RESPONSE: ' + evt.data + '</span>');
    //websocket.close();
}
function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function writeToScreen(message) {
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    output.appendChild(pre);
}
window.addEventListener("load", init, false);

