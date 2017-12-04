<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>

<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
        dl {
            background: none repeat scroll 0 0 #FAFAFA;
            margin: 8px 0;
            padding: 0;
        }

        dt {
            display: inline-block;
            width: 170px;
        }

        dd {
            display: inline-block;
            margin-left: 8px;
            vertical-align: top;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <hr/>
    <form method="get" action = "meals">
        <select name = "userId">
            <option disabled autofocus>Select user</option>
            <option value="1">User1</option>
            <option value="2">User2</option>
        </select>
        <button type="submit">Apply</button>
    </form>
    <hr/>
    <a href="meals?action=create">Add Meal</a>
    <hr/>
        <form method="get" action="meals">
            <dt>DateFrom:</dt>
            <dd><input type="date" name="dateFrom" value = "${param.dateFrom}"></dd>
            </dl>
            <dl>
            <dt>DateTo:</dt>
            <dd><input type="date" size=40 name="dateTo" value="${param.dateTo}"></dd>
            </dl>
            <dl>
            <dt>TimeFrom:</dt>
            <dd><input type="time" name="timeFrom" value="${param.timeFrom}"></dd>
            </dl>
            <dl>
            <dt>TimeTO:</dt>
            <dd><input type="time" name="timeTo" value = "${param.timeTo}"></dd>
            </dl>
            <button type="submit">Apply</button>
        </form>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Time</th>
            <th>Description</th>
            <th>Calories</th>
            <th>Whose Meal?</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDate(meal.dateTime.toLocalDate())}
                </td>
                <td>
                    ${fn:formatTime(meal.dateTime.toLocalTime())}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>${username}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>