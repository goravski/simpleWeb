<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<div class="form">
    <form action="<c:url value='/registration' />" method="post">
        <input type="hidden" name="command" value="registration" />
        <h2> Fill in registration form </h2> <br>
        <table style="box-decoration-break: slice">
        <tr>
            <td>Name:</td>
            <td><input type="text" name="name" ></td>
        </tr>
        <br>
        <tr>
            <td>Login:</td>
            <td><input type="text" name="login"></td>
        </tr>
        <br>
        <td>Password:</td>
        <td><input type="password" name="password"></td>
        </tr>
        </table>
        <br><br>
        <button type="submit">Register</button>
    </form>
    <form action="index.jsp" >
        <button type="submit">At home page</button>
    </form>
</div>
</body>
</html>
