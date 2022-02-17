<%--
  Created by IntelliJ IDEA.
  User: Евгений
  Date: 14.02.2022
  Time: 21:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>TError Page</title>
</head>
<body>
Request from ${pageContext.errorData.requestURI} is failed <br/>
Servlet name: ${pageContext.errorData.servletName} <br/>
Status code: ${pageContext.errorData.statusCode} <br/>
Exception: ${pageContext.exception} <br/>
Message from exception: ${pageContext.exception.message}
</body>
</html>
