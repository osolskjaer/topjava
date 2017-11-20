<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

    <table border="1px">
        <tr>
            <th>Описание</th>
            <th>Количество калорий</th>
            <th>Дата</th>
            <th>Время</th>
            <th colspan=2>Редактировать</th>
        </tr>
        <c:forEach items="${meals}" var = "element">
        <tr>
            <td><c:out value="${element.description}" /></td>
            <c:choose>
                <c:when test="${element.exceed}">
                    <td style="color: crimson" bgcolor="#eee8aa"><c:out value="${element.calories}" /></td>
                </c:when>
                <c:otherwise>
                    <td style="color: green" bgcolor="#98fb98"><c:out value="${element.calories}" /></td>
                </c:otherwise>
            </c:choose>
            <javatime:format value="${element.dateTime}" style="M-" var = "date"/>
            <javatime:format value="${element.dateTime}" style="-S" var = "time"/>
            <td><c:out value="${date}" /></td>
            <td><c:out value="${time}" /></td>
            <td><a href="meals?action=edit&id=<c:out value="${element.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${element.id}"/>">Delete</a></td>
        </tr>
        </c:forEach>
    </table>
    <p><a href="meals?action=insert">Add User</a></p>
</body>
</html>