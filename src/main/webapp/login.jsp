<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<div class="form" >
    <h1> Enter your login and password </h1> <br>
    <form  method="post" action="<c:url value='/login' />">
        <input type="hidden" name="command" value="login" />
        <table style="with: 100%">
        <tr>
            <td>Login:</td>
            <td><input type="text" required placeholder="login or e-mail" name="login"></td>
        </tr>
        <br>
        <tr>
            <td>Password:</td>
            <td><input type="text" required placeholder="input password" name="password"></td>
        </tr>
        </table>
        <br>
        <input class="button" type="submit" value="log in">
    </form>
</div>
</body>
</html>
