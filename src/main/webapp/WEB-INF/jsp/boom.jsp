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
        #bomb_list {
            display: block;
        }
    </style>
    <link href="${fjwa_root}/assets/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="${fjwa_root}/assets/css/error.css" rel="stylesheet">
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


            <a class="btn btn-primary" href="#" onclick="addBomb()">
                Add Bomb
            </a>

            <a class="btn btn-primary red" href="${fjwa_root}/admin/superBomb.html">
                DO NOT PRESS
            </a>

            <a class="btn btn-primary" href="#"  onclick="defuse()">
                defuse!
            </a>

            <a class="btn btn-primary" id="show_expired" href="#" onclick="toggleBombs(true)">
                Hide Expired
            </a>
<sec:authorize access="hasRole('ROLE_ADMIN')">
            <a class="btn btn-primary" href="#" onclick="removeAll()">
                Remove All
            </a>
</sec:authorize>

        </div>
        <div class="hero-unit" id="bomb_list">
              ${bombs}
          </div>



</div>
<div class="rmx-error-log">
    ${errors}
</div>
<script>

    var fjwa;
    function startSession() {
        fjwa = {
            bombs: null,
            update: function(data) {
                this.bombs = data;
            },
            showAll:false,
            root:'${fjwa_root}'
        };

        loadData();
        toggleBombs(false)
        updateBombs();

    }

    function loadData() {
        $.getJSON('<spring:url value="/updateBombs.json"/>', {
            ajax : 'true'
        }, fjwa.update);
    }

    function defuse() {
        $.getJSON('<spring:url value="/defuse.json"/>', {
                    ajax : 'true'
                }, fjwa.update
        );

    }

    function addBomb() {
        $.getJSON('<spring:url value="/addBomb.json"/>', {
                    ajax : 'true'
                }, fjwa.update
        );
    }

    function removeAll() {
        $.getJSON('${fjwa_root}/removeAll.json', {
            ajax : 'true'
        }, fjwa.update);
    }

    function toggleBombs(toggle) {
        if (toggle)
            fjwa.showAll = !fjwa.showAll;
        $(document).ready(function () {
            $('a#show_expired').html(fjwa.showAll ? "Hide Expired" : "Show Expired");
        });
    }

    function updateBombs() {
        //updateClientSide();

//        checkForErrors();
        updateBombsFromServer();

        window.requestAnimationFrame(updateBombs);

    }


    function updateClientSide() {
        $(document).ready( function (){
            var html = 'CLIENT SIDE:';
            var data = fjwa.bombs;
            if (data != null) {
                var len = data.length;
                for (var i = 0; i < len; i++) {
                    html += '<br/>' + data[i].description;//.toString
                }
            } else {
                html += "<br/>NULL";
            }
            $('p#bomb_list').html(html);
        });
    }

    function updateBombsFromServer() {
        $(document).ready(
                function() {
                    $.getJSON('<spring:url value="/updateBombs.json"/>', {
                        ajax : 'true'
                    }, function(data){

                        var html = '';
                        if (data != null) {
                            fjwa.bombs = data;
                            var len = data.length;
                            for (var i = 0; i < len; i++) {
                                if (fjwa.showAll || ( data[i].live && !data[i].outOfTime ) )
                                    html += '<br/><span id="bomb_'+data[i].id+'">' + data[i].description + '</span>';//.toString
                            }
                        } else {
                            html += "<br/>NULL";
                        }


                        $('div#bomb_list').html(html);
                    });

                });
    }




</script>




<script src="${fjwa_root}/js/jquery.js">
</script>
<script src="http://malsup.github.com/jquery.form.js"></script>


<script src="${fjwa_root}/assets/js/bootstrap.js">
</script>
<script src="${fjwa_root}/js/debug.js"></script>
</body>
</html>