<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add</title>
</head>
<body>
<table>
    <tr>
        <th>Id</th>
        <th>Name of product</th>
        <th>Describe</th>
        <th>Price per unit</th>
        <th>Quantity in storage</th>
    </tr>
    <tr>
        <form method="post" action="<c:url value="/storage"/>">
            <input type="hidden" name="command" value="add_storage">
            <td>${sessionScope.product.idProduct}</td>
            <td>${sessionScope.product.productName}</td>
            <td>${sessionScope.product.describe}</td>
            <td>${sessionScope.product.price}</td>
            <td>${requestScope.quantity}</td>
            <input type="hidden" name="quantity" value="${requestScope.quantity}">
            <td><input class="button" type="submit" value="Confirm add"></td>

        </form>
    </tr><br>
</table>
</body>
</html>
