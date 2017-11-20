<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
    <script src="//code.jquery.com/jquery-1.12.4.js"></script>
    <script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <title>UserPage</title>
</head>
<body>
<c:set var="dateFormat" value="dd-MM-yyyy"  />
<script>
    $(function() {
        $('input[name=date]').datepicker({dateFormat: "dd-mm-yy"});
    });
</script>
<table>
<form method="POST" action='edit' name="frmAddUser">
    <input type="hidden" name = "dateFormat" value="${dateFormat}"/>
    <javatime:format value="${meal.dateTime}" pattern="${dateFormat}" var = "date"/>
    <javatime:format value="${meal.dateTime}" style="-S" var = "time"/>
    <tr>
        <td>ID :</td>
        <td><input type="text" readonly="readonly" name="id"
                   value="<c:out value="${meal.id}" />" /> <br /></td>
    </tr>
    <tr>
        <td>Описание :</td>
        <td><input type="text" name="description"
                    value="<c:out value="${meal.description}" />" /> <br /></td>
    </tr>
    <tr>
        <td>Количество калорий : </td>
        <td><input type="text" name="calories"
                    value="<c:out value="${meal.calories}" />" /> <br />  </td>
    </tr>
    <tr>
        <td>Дата :</td>
        <td><input type="text" name="date"
                    value="<c:out value="${date}" />" /> <br /></td>
    </tr>
    <tr>
        <td>Время : </td>
        <td><input type="text" name="time"
                   value="<c:out value="${time}" />" /> <br /> </td>
    </tr>
    <tr>
        <td><input
        type="submit" value="Submit" /></td>
    </tr>
    <c:if test="${!empty wrongFields}">
    <tr>
        <td colspan="2"><span style="color: red">Please, write correct values</span></td>
    </tr>
    </c:if>
</form>
</table>

</body>
</html>
