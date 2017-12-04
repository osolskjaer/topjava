<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Users</h2>
<section>
    <a href="users?action=create">Add User</a>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Calories</th>
            <th>Role</th>
        </tr>
        </thead>
        <c:forEach items="${users}" var="user">
            <jsp:useBean id="user" scope="page" type="ru.javawebinar.topjava.model.User"/>
            <tr>
                <td>
                    ${user.id}
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
<%--
                        ${fn:formatDateTime(meal.dateTime)}
--%>
                </td>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.caloriesPerDay}</td>
                <td>${user.roles}</td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>