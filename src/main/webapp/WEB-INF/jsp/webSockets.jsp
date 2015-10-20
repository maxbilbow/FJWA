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
        <div class="row">
            <h2>
                Welcome to WebSocket Testing page, <sec:authentication property="name"/>!
            </h2>

            <p>
                Do things with sockets (a little bit plagerised from <a href="https://www.websocket.org/echo.html">here...</a>)
            </p>
        </div>
        <%--<div class="bs-docs-grid">--%>
            <div class="row">
                <div class="col-md-6" >
                        <div class="form-group">
                            <label>
                                Message
                            </label>
                            <textarea id="sendMessage" name="name" class="form-control">Send one message.</textarea>
                        </div>

                        <div class="form-group">
                            <label>
                                To Socket
                            </label>
                            <select id="toSocket" onchange="updateUri()">
                                <option> --none--  </option>
                                <c:forEach items="${sockets}" var="socket">
                                    <option>${socket.uri}</option>
                                </c:forEach>
                            </select>
                        </div>
                    <div class="form-group">
                        <label>Secure</label>
                        <input type="checkbox" id="wss" value="false" onclick="updateWss()"/>
                    </div>
                        <input type="hidden"
                               name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>
                    <div class="btn btn-primary" onclick="connect()">Open Socket</div>
                    <div class="btn btn-primary" onclick="sendMessage()">Send</div>
                    <div class="btn btn-primary" onclick="disconnect()">Disconnect</div>
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
<script src="https://cdn.socket.io/socket.io-1.3.7.js"></script>


<%--<script src="assets/js/bootstrap.js"></script> --%>
</body>
</html>
