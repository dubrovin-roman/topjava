<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meal Form</title>
    <style>
        form {
            display: flex;
            flex-direction: column;
            font-size: 18px;
        }

        .group-input {
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<h3><a href="/topjava/index.html">Home</a></h3>
<hr>

<h2><c:out value="${meal == null ? 'Add New Meal' : 'Edit Meal'}"/></h2>

<form action="<c:out value="${meal == null ? 'insert' : 'update'}" />" method="post">

    <c:if test="${meal != null}">
        <input type="hidden" name="id" value="${meal.id}"/>
    </c:if>

    <div class="group-input">
        <label for="localDateTime">DateTime:</label>
        <input type="datetime-local" id="localDateTime" value="${meal.dateTime}" name="localDateTime">
    </div>

    <div class="group-input">
        <label for="description">Description:</label>
        <input type="text" id="description" value="<c:out value='${meal.description}' />" name="description">
    </div>

    <div class="group-input">
        <label for="calories">Calories:</label>
        <input type="number" id="calories" value="${meal.calories}" name="calories">
    </div>

    <div>
        <button type="submit">Save</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </div>

</form>
</body>
</html>
