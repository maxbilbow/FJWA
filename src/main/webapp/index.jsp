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
  <body onload="checkForErrors()">
    <div class="navbar navbar-fixed-top navbar-inverse">
      <div class="navbar-inner">
        <div class="container">
          <a class="brand" href="addGoal.html">
            Get started
          </a>
          <ul class="nav">
          </ul>
        </div>
      </div>
    </div>
    <div class="container">
      <div class="hero-unit">
        <div>
          <h1>
            Welcome to Fitness Tracker <sec:authentication property="name"/>!
          </h1>
          <p>
            To get started, we need to enter a goal for what we want to exercise for
            today.
          </p>
        </div>
        <a class="btn btn-primary" href="addGoal.html">
          Add Goal
        </a>

        <sec:authorize access="hasRole('ROLE_ADMIN')">
        <a class="btn btn-primary" href="editGoal.html">
          Edit Goal
        </a>
        </sec:authorize>
        
        <a class="btn btn-primary" href="addMinutes.html">
          Add Exercise Minutes
        </a>




        <a class="btn btn-primary" href="getGoalReports.html">
          getGoalReports
        </a>
        <a class="btn btn-primary" href="getGoals.html">
          getGoals
        </a>


        <a class="btn btn-warning" href="logout.html">
          Logout >>
        </a>
      </div>
      <div>
        <div class="hero-unit">
          <a class="btn btn-primary" href="gl.html">
            WebGL
          </a>
          <a class="btn btn-primary" href="boom.html">
            BOOM!
          </a>
          <a class="btn btn-primary" href="security.html">
            Security
          </a>
          <a class="btn btn-primary" href="goAway.html">
            Go Away!
          </a>
          </div>
      </div>
      <div class="hero-unit">
        <a class="btn btn-primary" href="rabbit.html">
          Rabbit Tester
        </a>
        <a class="btn btn-primary" href="webSockets.html">
          Web Sockets Tester
        </a>
        <a class="btn btn-primary" href="socketsExample.html">
          Web Sockets Tester
        </a>
        </div>
      <div class="hero-unit">
        <h3>Misc</h3>
        <a class="btn btn-primary" href="other.html">
          Other
        </a>
      </div>
      <div class="rmx-error-log">
      </div>
    </div>

    <script src="js/jquery.js">
    </script>
    <script src="js/debug.js"></script>
    
    <script src="assets/js/bootstrap.js">
    </script>
  </body>
</html>
