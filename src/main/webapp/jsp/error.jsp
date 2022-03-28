
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error Page</title>
</head>
<h2>There are errors: ${paramValues}</h2>
<body>
Error: ${requestScope.error}<br/>

Request from ${pageContext.errorData.requestURI} is failed <br/>
Servlet name: ${pageContext.errorData.servletName} <br/>
Status code: ${pageContext.errorData.statusCode} <br/>
Exception: ${pageContext.exception} <br/>
Message from exception: ${pageContext.exception.message}
<%--Object: ${requestScope.entrySet().forEach((k,v) -> {System.out.print(k+" : "+v)})}--%>
</body>
</html>
