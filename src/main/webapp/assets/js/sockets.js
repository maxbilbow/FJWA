/**
 * Created by bilbowm on 20/10/2015.
 */
var output;
var uri;
var wss = false;
var stompClient;
var socketLibs = {
    socketIo: 'socket.io',
    sockJs: 'SockJS'
}


var messageIds = [];

function useLibrary(lib) {
    var opt = wsUri();//$('select#socketLibrary option:selected').text();
    if (opt.indexOf("ws://") > -1 || opt.indexOf("wss://") > -1)
        opt = socketLibs.socketIo;
    else if (opt.indexOf("http://") > -1 || opt.indexOf("https://") > -1)
        opt = socketLibs.sockJs;
    return lib != null ? opt === lib : opt;
}

//function stayConnected(val) {
//    if (val)
//        $('#stayConnected').prop('checked', val);
//    else
//        return $('#stayConnected').prop('checked');
//}

function setConnected(connected) {
    updateUri();
    updateWss();
    if (connected == true) {
        $('input#customSocket').attr('disabled', 'disabled');
        $('input#customSocket').attr('hidden');
        //$('#wss').attr('disabled', 'disabled');
        $('select#toSocket').attr('disabled', 'disabled');
        $('div#openSocket').attr('disabled', 'disabled');
        $('div#closeSocket').removeAttr('disabled');
        $('div#sendButton').removeAttr('disabled');
        //$('select#socketLibrary').attr('disabled', 'disabled');
        $('input#chatBroker').attr('disabled', 'disabled');
        console.log('CONNECTED >>> ');
    } else {
        $('input#customSocket').removeAttr('disabled');
        $('input#customSocket').removeAttr('hidden');
        //$('#wss').removeAttr('disabled');
        $('select#toSocket').removeAttr('disabled');
        $('div#openSocket').removeAttr('disabled');
        $('div#closeSocket').attr('disabled', 'dissabled');
        $('div#sendButton').attr('disabled', 'disabled');
        //$('select#socketLibrary').removeAttr('disabled');
        $('input#chatBroker').removeAttr('disabled');
        console.log('DISCONNECTED <<<');
    }

}

function getMessage() {
    return $('textarea#sendMessage').val();
}

function wsUri() {
    updateUri();
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
        $('#wss').attr('disabled', 'disabled');
        $('input#customSocket').removeAttr('disabled');
        $('input#customSocket').removeAttr('hidden');//.prop('disabled', false);//.disabled(false);
    } else {
        $('#wss').removeAttr('disabled');
        $('input#customSocket').attr('disabled','disabled');
        $('input#customSocket').attr('hidden','hidden');
    }
    console.log("uri: '" + uri + "'");
}

function updateWss() {
    //wss = $('#wss').prop('checked');
    console.log("uri: '" + wsUri() + "'");
}

function sendMessage() {
    var message = getMessage();

    console.log("sending: '" + message + "', to: " + wsUri() + ', on topic...' + chatTopic());
    try {
        switch (useLibrary()) {
            case socketLibs.socketIo:
                if (window.websocket) {
                    writeToScreen("SENT: " + message);
                    websocket.send(message);
                } else {
                    console.log("NO CONNECTION!");
                }
                break;
            case socketLibs.sockJs:
                var id = Math.floor(Math.random() * 1000000);
                stompClient.send(chatTopic(), {
                    priority: 9
                }, JSON.stringify({
                    message: message,
                    id: id
                }));
                messageIds.push(id);
                break;
        }
    } catch (e) {
        writeErrToScreen(e);
    }
}
function chatBroker() {
    return $('input#chatBroker').val();
}

function chatTopic() {
    return $('input#chatTopic').val();
}
function disconnect() {

    if (window.websocket)
        websocket.close();
    if (stompClient) {
        stompClient.disconnect();
        writeToScreen("DISCONNECTED");
        setConnected(false);
    }
}

function getUsername() {
    return $('input#username').val();
}

function getPassword() {
    return $('input#password').val();
}

function connect(quietly) {
    try {
        disconnect();
        switch (useLibrary()) {
            case socketLibs.sockJs:
                var uri = wsUri();
                window.websocket = new SockJS(uri);//'/hello');
                var socket = window.websocket;
                stompClient = Stomp.over(socket);
                var headers = getUsername().length > 0 ? {
                    login: getUsername(),
                    passcode: getPassword(),
                    persistent:true,
                    // additional header
                    'client-id': 'fjwa',
                    'heart-beat':'30000,30000'
                } : {};
                stompClient.connect(headers, function (frame) {
                        writeToScreen('Connected: ' + frame);
                    setConnected(true);
                    stompClient.subscribe(chatBroker(), function(data) {
                        writeToScreen(data);
                    });
                    stompClient.onclose = function () {
                        setConnected(false);
                    };
                    stompClient.heartbeat.outgoing = 10000;
                });

                break;
            case socketLibs.socketIo:

                window.websocket = new WebSocket(wsUri());
                websocket.onopen = function (evt) {
                    onOpen(evt);
                };
                websocket.onclose = function (evt) {
                    onClose(evt)
                };
                websocket.onmessage = function (evt) {
                    onMessage(evt)
                };
                websocket.onerror = function (evt) {
                    onError(evt);

                };
                break;
        }
    } catch (e) {
        writeErrToScreen(e);
    }
}


function onOpen(evt) {
    writeToScreen("CONNECTED");
    setConnected(true);
}
function onClose(evt) {

    writeToScreen("DISCONNECTED");
    setConnected(false);
}
function onMessage(evt) {
    writeToScreen(evt.data, '<span style="color: rgb(151, 253, 255);">RESPONSE:</span> ');
    //websocket.close();
}
function onError(evt) {
    writeToScreen('<span style="color: rgba(255, 170, 167, 1);">ERROR:</span> ' + evt.data);
}

function writeErrToScreen(err) {
    writeToScreen('<span style="color: rgba(255, 170, 167, 1);">ERROR:</span> ' + err);
}

function parseList(data) {
    var result = 'RESULT::';
    var json = JSON.parse(data);
    for (var i=0;i<json.length;++i) {
        result += '<br/>ENTRY ' + i + ': ' + json[i];
        //var element = JSON.parse(data[i]);
        $.each(json[i], function (k, v) {
            //display the key and value pair
            result += '<br/> --> ' + k + ': ' + v;
        });
    }
    return result;
}

function tryParse(data, prefix) {
    //if( Object.prototype.toString.call( data ) === '[object Array]' ) {

    if (data.body) {
        var parsed = '';
            try {
                var json = JSON.parse(data.body);
                var message = json.message, time = json.time;
                if (time)
                    var time = new Date(time),
                        h = time.getHours(), // 0-24 format
                        m = time.getMinutes();
                parsed += h + ':' + m + " >> ";

                if (message)
                    parsed += message;
            } catch (e) {
                console.log(e);
                return "As String >> " + data.body;
            }

        return parsed.length > 0 ? parsed : 'UN-PARSED: ' + json;
    } else {
        try {
            //var json = JSON.parse(data);
            //console.log(json);
            var parsed = parseList(data);
            return parsed;
        } catch (e) {
            console.log('Could not parse as JSON: ' + data);
            console.log(e);
            return (prefix ? prefix : '') + data;
        }

    }
}

function writeToScreen(message, prefix) {
    var pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = tryParse(message, prefix);
    output.appendChild(pre);
    //console.log(message);
    try {
        output.scrollTop = output.scrollHeight;
    } catch (e) {
        console.log(e);
    }
}


window.addEventListener("load", init, false);
