<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>
    </title>

    <jsp:include page="header.jsp"/>
    <style>
        #output {
            height: 200px;
            overflow-y: scroll;
            background-color: #000000;
            color: #ffffff;
            padding: 5px;
        }
        #customSocket {
            /*width: 100%;*/
        }
    </style>
</head>
<body  onload="startUpdates()">
<div class="navbar navbar-fixed-top navbar-inverse">
    <div class="navbar-inner">
        <div class="container">
            <a class="brand" href="/FJWA">
                Home
            </a>
            <ul class="nav">
            </ul>
        </div>
    </div>
</div>
<div class="container">
    <div class="hero-unit">
        <div class="row">
            <h2>
                Welcome to WebSocket Testing page, <sec:authentication property="name"/>!
            </h2>

            <p>
                Do things with sockets (a little bit plagerised from <a href="https://www.websocket.org/echo.html">here...</a>)
            </p>
            <p>
                Some known channels on localhost:8080/spring-ng-chat/:
                <ul>
            <li>'/app/chat' (replies to '/topic/messages')</li>
            <li>'/app/debug/logs' (replies to '/topic/debug/logs')</li>
            <li>'/app/debug/errors' (replies to '/topic/debug/errors')</li>
        </ul>
            </p>
        </div>
        <%--<div class="bs-docs-grid">--%>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label>
                        Message
                    </label>
                    <textarea id="sendMessage" name="name" class="form-control">Send one message.</textarea>
                </div>
                <div class="form-group">
                    <label>Send On:</label>
                    <input type="text" id="chatTopic" value="/app/chat"/>
                    <label>Listen To:</label>
                    <input type="text" id="chatBroker" value="/topic/**"/>
                </div>


                <div class="form-group">
                    <label>
                        To Socket
                    </label>
                    <select id="toSocket" onchange="updateUri()">
                        <option> --custom--</option>
                        <c:forEach items="${sockets}" var="socket">
                            <option>${socket}</option>
                        </c:forEach>
                    </select>
                    <input id="customSocket" type="text" value="ws://" onchange="updateUri()" disabled="true">
                </div>
                <div class="form-group">
                    <label>
                        Using
                    </label>
                    <select id="socketLibrary">
                        <option>socket.io</option>
                        <option>SockJS</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Secure</label>
                    <input type="checkbox" id="wss" value="false" onclick="updateWss()"/>
                </div>
                <input type="hidden"
                       name="${_csrf.parameterName}"
                       value="${_csrf.token}"/>

                <div id="openSocket" class="btn btn-primary" onclick="connect()">Open Socket</div>
                <div id="sendButton" class="btn btn-primary" onclick="sendMessage()">Send</div>
                <div id="closeSocket" class="btn btn-primary" onclick="disconnect()">Disconnect</div>
            </div>
            <div class="col-md-6">

                <div class="right" id="output">

                </div>
            </div>
        </div>
        <%--</div>--%>
        <div class="row rmx-error-log">
        </div>
    </div>


</div>

<%--<div ng-controller="ChatCtrl" class="container">--%>
    <%--<form ng-submit="addMessage()" name="messageForm">--%>
        <%--<input type="text" placeholder="Compose a new message..." ng-model="message"/>--%>

        <%--<div class="info">--%>
            <%--<span class="count" ng-bind="max - message.length" ng-class="{danger: message.length > max}">140</span>--%>
            <%--<button ng-disabled="message.length > max || message.length === 0">Send</button>--%>
        <%--</div>--%>
    <%--</form>--%>
    <%--<hr/>--%>
    <%--<p ng-repeat="message in messages | orderBy:'time':true" class="message">--%>
        <%--<time>{{message.time | date:'HH:mm'}}</time>--%>
        <%--<span ng-class="{self: message.self}">{{message.message}}</span>--%>
    <%--</p>--%>
<%--</div>--%>
<script type="text/javascript" src="js/sockets.js"></script>
<script type="text/javascript">
    function update() {
        checkForErrors();
        window.requestAnimationFrame(update);

    }


    function startUpdates() {


        update();
    }

</script>
<%--ng-app="chatApp"--%>
<script src="https://cdn.socket.io/socket.io-1.3.7.js"></script>
<script src="libs/sockjs/sockjs.min.js" type="text/javascript"></script>
<script src="libs/stomp-websocket/lib/stomp.min.js" type="text/javascript"></script>
<script src="libs/angular/angular.min.js"></script>
<script src="libs/lodash/dist/lodash.min.js"></script>
<%--<script src="app/app.js" type="text/javascript"></script>--%>
<%--<script src="app/controllers.js" type="text/javascript"></script>--%>
<%--<script src="app/services.js" type="text/javascript"></script>--%>

<%--<script src="assets/js/bootstrap.js"></script> --%>
</body>
</html>
