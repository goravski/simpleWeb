<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User</title>
</head>
<body>

<table>
    <form>
        <tr>
            <h3>Users data</h3>
            <td></td>
            <c:out value="${sessionScope.person.toString()}" default="User not found"/>
        </tr>
        <br>
    </form>
</table>

</body>
</html>
