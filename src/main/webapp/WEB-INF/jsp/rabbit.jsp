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
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <!-- Le styles -->
  <link href="assets/css/bootstrap.css" rel="stylesheet">
  <style>
    body { padding-top: 60px; /* 60px to make the container go all the way
      to the bottom of the topbar */ }
  </style>
  <link href="assets/css/bootstrap-responsive.css" rel="stylesheet">
  <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js">
  </script>
  <![endif]-->
  <!-- Le fav and touch icons -->
  <link rel="shortcut icon" href="assets/ico/favicon.ico">
  <link rel="apple-touch-icon-precomposed" sizes="144x144" href="assets/ico/apple-touch-icon-144-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="114x114" href="assets/ico/apple-touch-icon-114-precomposed.png">
  <link rel="apple-touch-icon-precomposed" sizes="72x72" href="assets/ico/apple-touch-icon-72-precomposed.png">
  <link rel="apple-touch-icon-precomposed" href="assets/ico/apple-touch-icon-57-precomposed.png">
  <link href="assets/css/error.css" rel="stylesheet">
  <style>
  </style>
</head>
<body onload="startUpdates()">
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
    <div>
      <h2>
        Welcome to RabbitMQ Testing page, <sec:authentication property="name"/>!
      </h2>
      <p>
        send a message
      </p>
    </div>

    <form:form commandName="aMessage">
      <c:if test="${param.error != null}">
        <div class="errorblock">
          Whoops! <br />
          Caused: ${param.error} <br/>
        </div>
      </c:if>
      <div class="control-group">
        <label for="textinput1">
          Message
        </label>
        <form:input path="message" value="${aMessage.message}" />
      </div>

      <div class="control-group">
        <label for="selectinput1">
          Topic
        </label>
        <form:select id="topics" path="topic" items="${topics}" />
      </div>
      <input type="hidden"
             name="${_csrf.parameterName}"
             value="${_csrf.token}"/>
      <input type="submit" class="btn btn-primary" value="Send >> " />

    </form:form>

  </div>
  <div class="rmx-error-log">
  </div>
</div>


  <script type="text/javascript">
    function update() {
      checkForErrors();
      window.requestAnimationFrame(update);

    }

    function startRabbitServer() {
      var amqp = require('amqplib/callback_api');
      amqp.connect('amqp://localhost', function(err, conn) {

        conn.createChannel(function(err, ch) {
          var q = 'hello';

          ch.assertQueue(q, {durable: false});
        });
      });
      console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q);
      ch.consume(q, function(msg) {
        console.log(" [x] Received %s", msg.content.toString());
      }, {noAck: true});
    }


    function startUpdates() {

//      startRabbitServer();
      update();
    }

  </script>
<script src="js/jquery.js">
</script>
<script src="js/debug.js"></script>

<script src="assets/js/bootstrap.js">
</script>
</body>
</html>
