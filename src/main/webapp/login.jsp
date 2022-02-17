<%--
  Created by IntelliJ IDEA.
  User: Евгений
  Date: 13.02.2022
  Time: 13:10
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<div class="form">
    <h1> Enter your login and password </h1> <br>
    <form  method="post" action="/login">
        <input type="hidden" name="command" value="login" />
        Login:<br/>
        <input type="text" required placeholder="login or e-mail" name="login"><br><br>
        <br/>Password:<br/>
        <input type="text" required placeholder="input password" name="password"> <br><br>
        <br/>
        ${errorLoginPassMessage}
        <br/>
        ${wrongAction}
        <br/>
        ${nullPage}
        <br>
        <input class="button" type="submit" , value="log in">
    </form>
</div>
</body>
</html>
