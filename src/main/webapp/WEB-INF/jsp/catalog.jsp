<%--
  Created by IntelliJ IDEA.
  User: Isa
  Date: 19.12.2024
  Time: 11:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="header.jsp"%>

<html>
<head>
    <title>Books</title>
</head>
<body>
    <form method="get" action="${pageContext.request.contextPath}/catalog">
        <h1><fmt:message key="page.header.text"/></h1>
        <c:forEach items="${requestScope.books}" var="book">
            <li>
                <a href="${pageContext.request.contextPath}/bookPage?bookId=${book.id}">${book.title}</a>
            </li>
        </c:forEach>
    </form>
</body>
</html>
