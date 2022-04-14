<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Product</title>
</head>
<body>
<h1>Product operation</h1>
<form method="get" action="<c:url value='/get_product'/>">
    <input type="hidden" name="command" value="get_product"/>
    <h2> Find product:</h2>
    <table style="alignment-baseline: baseline">
        <tr>
            <td>Id:</td>
            <td><input type="number" name="id"></td>
            <td><input class="button" type="submit" value="get Product"></td>
        </tr>
    </table>
</form>
<h3>${requestScope.info}</h3>
<c:set var="id" scope="page" value="${sessionScope.product.idProduct}"/>
<c:choose>
    <c:when test="${id != 0}">
        <form method="post" action="<c:url value='/update_product'/>">
            <input type="hidden" name="command" value="update_Product"/>
            <table style="alignment-baseline: baseline">
                <tr>Product data:</tr>
                <tr>
                    <td>Id:</td>
                    <td><input name="id" value="${sessionScope.product.idProduct}"></td>
                </tr>
                <tr>
                    <td>Name:</td>
                    <td><input name="name" value="${sessionScope.product.productName}"></td>
                </tr>
                <tr>
                    <td>Price:</td>
                    <td><input name="price" value="${sessionScope.product.price.value}"></td>
                </tr>
                <tr>
                    <td>Describe:</td>
                    <td><input name="describe" value="${sessionScope.product.describe}"></td>
                </tr>
                <tr>
                    <td>Quantity:</td>
                    <td><input name="quantity" value="${requestScope.storage.quantity}"></td>
                </tr>
                <br>
                <td><input class="button" type="submit" value="Update Product"></td>
            </table>
        </form>
        <form method="get" action="<c:url value="/get_storage"/>">
            <input type="hidden" name="command" value="get_storage">
            <input class="button" type="submit" value="Chek quantity">
        </form>
        <form method="post" action="<c:url value="/product"/>">
            <input type="hidden" name="command" value="delete_product">
            <input class="button" type="submit" value="Delete Product">
        </form>
    </c:when>
    <c:otherwise>
        <form method="post" action="<c:url value='/add_product'/>">
            <input type="hidden" name="command" value="add_product">
            <table style="alignment-baseline: baseline">
                <h3> Product with such ID not exist. Insert new product:</h3>
                <tr>
                    <td>Id:</td>
                    <td><input type="text" name="id"></td>
                </tr>
                <tr>
                    <td>Name:</td>
                    <td><input type="text" name="name"></td>
                </tr>
                <tr>
                    <td>Price:</td>
                    <td><input type="text" name="price"></td>
                </tr>
                <tr>
                    <td>Quantity:</td>
                    <td><input type="text" name="quantity"></td>
                </tr>
                <tr>
                    <td>Describe:</td>
                    <td><input type="text" name="describe"></td>
                </tr>
                <br>
                <td><input class="button" type="submit" value="Add Product"></td>
            </table>
        </form>
    </c:otherwise>
</c:choose>
<br>

</body>
</html>
