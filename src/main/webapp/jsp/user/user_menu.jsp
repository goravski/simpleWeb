
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"  %>
<html>
<head>
    <title>USER</title>

</head>
<body>

<h1>Hello ${sessionScope.person.name} Cart ${sessionScope.cart.id}!</h1>

<form method="get" action="<c:url value='/get_product_page'/>">
    <table style="alignment-baseline: baseline">
        <input type="hidden" name="command" value="get_product_page"/>

        <input type="hidden" name="currentPage" value="1"/>
        <input class="button" type="submit" value="See Catalog">
    </table>
</form>
<c:if test= "${requestScope.pagination.currentPage > 0}">
    <c:import url="/display_products.jsp"/>
</c:if>
<br><br>

<form method="post" action="<c:url value="/cart_buy"/> ">
    <input type="hidden" name="command" value="add_cart">
    <input class="button" type="submit" value="View cart: ${sessionScope.cart.orderList.size()} products">
</form>

<form method="post" action="<c:url value='/logout'/>">
    <table style="alignment-baseline: baseline">
        <input type="hidden" name="command" value="logout"/>
        <input class="button" type="submit" value="log out">
    </table>
</form>
</body>
</html>
