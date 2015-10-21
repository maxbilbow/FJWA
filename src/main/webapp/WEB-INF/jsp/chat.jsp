<%--
  Created by IntelliJ IDEA.
  User: bilbowm
  Date: 21/10/2015
  Time: 16:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700" rel="stylesheet" type="text/css" />
    <link href="assets/css/chat.css" rel="stylesheet" type="text/css" />
</head>
<body ng-app="chatApp">
<div ng-controller="ChatCtrl" class="container">
    <form ng-submit="addMessage()" name="messageForm">
        <input type="text" placeholder="Compose a new message..." ng-model="message" />
        <div class="info">
            <span class="count" ng-bind="max - message.length" ng-class="{danger: message.length > max}">140</span>
            <button ng-disabled="message.length > max || message.length === 0">Send</button>
        </div>
    </form>
    <hr />
    <p ng-repeat="message in messages | orderBy:'time':true" class="message">
        <time>{{message.time | date:'HH:mm'}}</time>
        <span ng-class="{self: message.self}">{{message.message}}</span>
    </p>
</div>

<script src="//cdn.jsdelivr.net/sockjs/0.3.4/sockjs.min.js"></script>
<%--<script src="assets/js/sockjs/sockjs.min.js" type="text/javascript"></script>--%>
<script src="assets/js/stomp.js" type="text/javascript"></script>
<script src="assets/js/angular/angular.min.js"></script>
<script src="assets/js/lodash/dist/lodash.min.js"></script>
<script src="assets/js/chat.js" type="text/javascript"></script>
<%--<script src="assets/js/controllers.js" type="text/javascript"></script>--%>
<%--<script src="assets/js/services.js" type="text/javascript"></script>--%>
</body>
</html>