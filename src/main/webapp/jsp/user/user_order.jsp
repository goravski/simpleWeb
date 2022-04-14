<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Order of ${sessionScope.person.name}</title>
</head>
<body>
<table border="1">
    <form method="post" action="<c:url value="/buy_product" />">
        <input type="hidden" name="command" value="order_put_to_cart">
        <tr>
            <th>Name of product</th>
            <th>Describe</th>
            <th>Price per unit</th>
            <th>Units to buy</th>
            <th>Cost</th>
        </tr>
        <tr>
            <td>${sessionScope.order.product.productName}</td>
            <td>${sessionScope.order.product.describe}</td>
            <td>${sessionScope.order.product.price}</td>
            <td>${sessionScope.order.orderQuantity}</td>
            <td>${sessionScope.order.cost}</td>
            <td><input class="button" type="submit" value="Buy"></td>
        </tr>
    </form>


</table>

</body>
</html>
