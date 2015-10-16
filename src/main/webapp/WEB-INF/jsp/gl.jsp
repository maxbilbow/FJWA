<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

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
    .red {
      background-color: red;
    }
    #bomb_list {
      display: block;
    }
  </style>
  <link href="assets/css/bootstrap-responsive.css" rel="stylesheet">
  <link href="assets/css/error.css" rel="stylesheet">
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
  <style>
  </style>



</head>
<body  onload="startSession()">
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
    <canvas id="gl">

    </canvas>


    <div>
      <a class="btn btn-primary" href="#" onclick="glrun('triangles',false)">GL_TRIANGLES</a>
      <a class="btn btn-primary" href="#" onclick="glrun('wireframe',false)">GL_LINE_STRIP</a>
      <a class="btn btn-primary" href="#" onclick="glrun('points',false)">GL_POINTS</a>
      <a class="btn btn-primary" href="#" onclick="toggleBackground()">Background</a>
      <a class="btn btn-primary" href="#" onclick="showLog()">See Log</a>


    </div>
  </div>

</div>
<div class="rmx-error-log">

</div>

<script>
  var fjwa = {}
  fjwa.root = '/FJWA';
  function startSession() {

    glrun('triangles',true);
    update();

  }

  function update() {
    checkForErrors();

    window.requestAnimationFrame(updateBombs);

  }
</script>




<script src="js/jquery.js">
</script>
<script src="http://malsup.github.com/jquery.form.js"></script>

<script src="js/gl.js"></script>
<script src="js/bootstrap.js">
</script>
<script src="js/debug.js"></script>
</body>
</html>