<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>ADMIN</title>
</head>
<body>

<h1>Hello ADMIN!</h1>


<form method="get" action="<c:url value='/get_user'/>">
    <input type="hidden" name="command" value="get_User"/>
    <h2> Blocking user</h2> <br>
    <table style="alignment-baseline: baseline">
        <tr>
            <td>Login:</td>
            <td><input type="text" name="login"></td>
            <td><input class="button" type="submit" value="get User"></td>
        </tr>
        <br>

    </table>
</form>
<form method="post" action="<c:url value='/update_user'/>">
    <input type="hidden" name="command" value="update_User"/>
    <tr>
        <table style="box-decoration-break: slice">
            <tr>Users data</tr><br>
            <tr>
                <td>Id:</td>
                <td><input name="id" value="${requestScope.person.id}"></td>
            </tr>
            <tr>
                <td>Name:</td>
                <td><input name="name" value="${requestScope.person.name}"></td>
            </tr>
            <br>
            <tr>
                <td>Login:</td>
                <td><input name="login" value="${requestScope.person.login}"></td>
            </tr>
            <br>
            <tr>
                <td>Role:</td>
                <td><input name="role" value="${requestScope.person.role}"></td>
            </tr>
            <br>
            <tr>
                <td>Status:</td>
                <c:set var="alter" scope="page" value="${requestScope.person.status}"/>
                <c:choose>
                    <c:when test="${alter.equals('Open')}">
                        <td><input type="radio" name="status" value="Open"  checked> Open</td>
                        <td><input type="radio" name="status" value="Close"> Block</td>
                    </c:when>
                    <c:when test="${alter.equals('Close')}">
                        <td><input type="radio" name="status" value="Open"> Unblock</td>
                        <td><input type="radio" name="status"  value="Close" checked> Close</td>
                    </c:when>
                    <c:otherwise>
                        status not identified
                    </c:otherwise>
                </c:choose>

            </tr><br>
            <td><input class="button" type="submit" value="Accept"></td>
        </table>
    <br>
</form>
<form method="post" action="<c:url value='/logout'/>">
    <table style="alignment-baseline: baseline">
        <input type="hidden" name="command" value="logout"/>
        <input class="button" type="submit" value="log out">
    </table>
</form>


</body>
</html>
