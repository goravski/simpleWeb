<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Cart Get</title>
</head>
<body>
<form action="<c:url value="/cart_buy"/>" method="post" id="this">
    <input type="hidden" name="command"   value="get_cart"/>
</form>
<script>
    setTimeout(function () {
        document.getElementById('this').submit();
    },0);
</script>
</body>
</html>
