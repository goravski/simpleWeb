<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1>User operation</h1>
<form method="get" action="<c:url value='/get_user'/>">
    <input type="hidden" name="command" value="get_User"/>
    <h2> Find user:</h2>
    <table style="alignment-baseline: baseline">
        <tr>
            <td>Login:</td>
            <td><input type="text" name="login"></td>
            <td><input class="button" type="submit" value="get User"></td>
        </tr>
    </table>
</form>

<form method="post" action="<c:url value='/update_user'/>">
    <c:set var="id" scope="page" value="${requestScope.person.id}"/>
    <c:choose>
        <c:when test="${id != 0}">
            <table style="alignment-baseline: baseline">
                <tr>Users data:</tr>
                <tr>
                    <td>Id:</td>
                    <td><input name="id" value="${requestScope.person.id}"></td>
                </tr>
                <tr>
                    <td>Name:</td>
                    <td><input name="name" value="${requestScope.person.name}"></td>
                </tr>
                <tr>
                    <td>Login:</td>
                    <td><input name="login" value="${requestScope.person.login}"></td>
                </tr>
                <tr>
                    <td>Role:</td>
                    <td><input name="role" value="${requestScope.person.role}"></td>
                </tr>
                <br><br>
                <tr>
                    <td>Status:</td>
                    <c:set var="alter" scope="page" value="${requestScope.person.status}"/>
                    <c:choose>
                        <c:when test="${alter.equals('Open')}">
                            <td><input type="radio" name="status" value="Open" checked> Open</td>
                            <td><input type="radio" name="status" value="Close"> Block</td>
                        </c:when>
                        <c:when test="${alter.equals('Close')}">
                            <td><input type="radio" name="status" value="Open"> Unblock</td>
                            <td><input type="radio" name="status" value="Close" checked> Close</td>
                        </c:when>
                        <c:otherwise>
                            <td>status not identified</td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <br><br>
                <tr>
                    <input type="hidden" name="command" value="update_user"/>
                    <td><input class="button" type="submit" value="Update User"></td>
                    <br>
                </tr>
            </table>

        </c:when>
        <c:otherwise>
            <h2>User not found</h2>
        </c:otherwise>
    </c:choose>

    <br>
</form>

</body>
</html>
