
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>success</title>
</head>
<body>
<h1>Registration is successful</h1>
<table style="box-decoration-break: slice">
    <tr>
        <td>Name:</td>
        <td>${requestScope.person.name}</td>
    </tr>
    <br>
    <tr>
        <td>Login:</td>
        <td>${requestScope.person.login}</td>
    </tr>

</table>
<br><br><br>
<a href="/login.jsp">Sign in</a> <br> <br>
</body>
</html>
