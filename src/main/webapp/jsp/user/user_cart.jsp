
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Cart </title>
</head>
<body>
<h2>Cart ${sessionScope.cart.id} orders of ${sessionScope.person.name} </h2>

<table>
    <tr>
        <td>Product name</td>
        <td>Price</td>
        <td>Quantity</td>
        <td>Cost</td>
    </tr>
    <c:forEach var="order" items="${sessionScope.cart.orderList}">
        <form method="post" action="<c:url value="/order"/> ">
            <input type="hidden" name="command" value="order_delete_list">
            <tr>
                <td>${order.product.productName}</td>
                <td>${order.product.price}</td>
                <td>${order.orderQuantity}</td>
                <td>${order.cost}</td>
                <input type="hidden" name="id" value="${order.id}"/>
                <td><input class="button" type="submit" value="Delete"></td>
            </tr>
        </form>
    </c:forEach>
    <tr>Total cost ${sessionScope.cart.cost}</tr>

    <form method="post" action="<c:url value="/cart_buy"/> ">
        <input type="hidden" name="command" value="order_add">
        <td><input class="button" type="submit" value="Payment"></td>
    </form>

    <form method="get" action="<c:url value="/user_menu"/> ">
        <input type="hidden" name="command" value="user_home">
        <td><input class="button" type="submit" value="Home"></td>
    </form>
</table>

</body>
</html>
