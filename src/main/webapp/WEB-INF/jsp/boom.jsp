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
    <link href="${fjwa_root}/assets/css/bootstrap.css" rel="stylesheet">
    <style>
        body { padding-top: 60px; /* 60px to make the container go all the way
      to the bottom of the topbar */ }
        .red {
            background-color: red;
        }
        #bomb-list {
            display: block;
        }
    </style>
    <link href="${fjwa_root}/assets/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="${fjwa_root}/assets/css/error.css" rel="stylesheet">


</head>
<body >
<div class="navbar navbar-fixed-top navbar-inverse">
    <div class="navbar-inner">
        <div class="container">
            <a class="brand" href="${fjwa_root}/">
                Home
            </a>
            <ul class="nav">
            </ul>
        </div>
    </div>
</div>
<div class="container">

        <div class="hero-unit">
            <h1>${boom}</h1>


            <span class="btn btn-primary" id="add-bomb">
                Add Bomb
            </span>

            <a class="btn btn-primary red" href="${fjwa_root}/admin/superBomb.html">
                DO NOT PRESS
            </a>

            <span class="btn btn-primary" id="defuse">
                defuse!
            </span>

            <span class="btn btn-primary" id="show-expired">
                Show Expired
            </span>
<sec:authorize access="hasRole('ROLE_ADMIN')">
            <span class="btn btn-primary" id="remove-all">
                Remove All
            </span>
</sec:authorize>

        </div>
        <div class="hero-unit" id="bomb-list">

          </div>



</div>
<div class="rmx-error-log">
    ${errors}
</div>
<script data-main="${fjwa_root}/assets/js/boom" src="${fjwa_root}/assets/js/require.min.js"></script>




<script src="${fjwa_root}/assets/js/bootstrap.js">
</script>
<script src="${fjwa_root}/js/debug.js"></script>
</body>
</html>