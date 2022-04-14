<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Products</title>
</head>
<body>

<table border="1">
    <tr>
        <th>Name of product</th>
        <th>Describe</th>
        <th>Price per unit</th>
        <th>Units to buy</th>
    </tr>

    <c:forEach var="product" items="${sessionScope.products}">
        <tr>
            <form method="post" action="<c:url value='/buy_product'/>">
                <input type="hidden" name="command" value="order_put_list">

                <td>${product.productName}</td>
                <td>${product.describe}</td>
                <td>${product.price}</td>
                <td><input type="text" name="quantity"></td>
                <input type="hidden" name="id" value="${product.idProduct}">
                <td><input class="button" type="submit" value="Put in Cart"></td>
            </form>
        </tr>

    </c:forEach>

</table>


<%--Display page numbers--%>
<table border="1" cellpadding="5" cellspacing="5">

    <tr>
        <%-- Display reference to previous link--%>
        <c:if test="${requestScope.pagination.currentPage !=1}">

            <td>
                <a href="/get_product_page?command=get_product_page&currentPage=${requestScope.pagination.currentPage-1}">Previous</a>
            </td>

        </c:if>
        <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
            <c:choose>
                <c:when test="${requestScope.pagination.currentPage eq i}">
                    <td>${i}</td>
                </c:when>
                <c:otherwise>
                    <td><a href="/get_product_page?command=get_product_page&currentPage=${i}">${i}</a></td>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <%--        Display next link--%>
        <c:if test="${requestScope.pagination.currentPage < requestScope.totalPages}">
            <td>
                <a href="/get_product_page?command=get_product_page&currentPage=${requestScope.pagination.currentPage + 1}">Next</a>
            </td>
        </c:if>
    </tr>
</table>

</body>
</html>
