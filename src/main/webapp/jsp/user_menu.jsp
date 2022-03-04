
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmy" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8"  %>
<html>
<head>
    <title>USER</title>

</head>
<body>

<h1>Hello USER!</h1>
<form method="post" action="<c:url value='/logout'/>">
    <table style="alignment-baseline: baseline">
        <input type="hidden" name="command" value="logout"/>
        <input class="button" type="submit" value="log out">
    </table>
</form>
</body>
</html>
