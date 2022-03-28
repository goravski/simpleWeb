<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>user success</title>
</head>
<body>
<h1>Add user is successful</h1>

<table style="box-decoration-break: slice">
    <tr>
        <td>User added:</td>
        <td>${requestScope.person}</td>
    </tr>
    <br>
    <tr>
        <td>Name:</td>
        <td>${requestScope.person.toString()}</td>
    </tr>

</table>
<br><br><br>
<a href="login.jsp">Sign in</a> <br> <br>
</body>
</html>
