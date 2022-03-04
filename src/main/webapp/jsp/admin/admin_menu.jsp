<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>ADMIN</title>
</head>
<body>

<h1>Hello ADMIN!</h1>

<c:import url="user_data.jsp"/>
<c:import url="/jsp/product_data.jsp"/>
<form method="post" action="<c:url value='/logout'/>">
    <table style="alignment-baseline: baseline">
        <input type="hidden" name="command" value="logout"/>
        <input class="button" type="submit" value="log out">
    </table>
</form>


</body>
</html>
