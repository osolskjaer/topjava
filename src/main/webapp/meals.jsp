<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

    <table border="1px">
        <tr>
            <th><c:out value="Описание" /></th>
            <th><c:out value="Количество калорий" /></th>
            <th><c:out value="Дата" /></th>
            <th><c:out value="Время" /></th>
        </tr>
        <c:forEach items="${meals}" var = "element">
        <tr>
            <td><c:out value="${element.description}" /></td>
            <c:choose>
                <c:when test="${element.exceed}">
                    <td style="color: crimson"><c:out value="${element.calories}" /></td>
                </c:when>
                <c:otherwise>
                    <td style="color: green"><c:out value="${element.calories}" /></td>
                </c:otherwise>
            </c:choose>
            <javatime:format value="${element.dateTime}" style="M-" var = "date"/>
            <javatime:format value="${element.dateTime}" style="-S" var = "time"/>
            <td><c:out value="${date}" /></td>
            <td><c:out value="${time}" /></td>
        </tr>
        </c:forEach>
    </table>
</body>
</html>