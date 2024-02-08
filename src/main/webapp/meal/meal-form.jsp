<%--
  Created by IntelliJ IDEA.
  User: dubro
  Date: 08.02.2024
  Time: 21:23
  To change this template use File | Settings | File Templates.
--%>
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
<c:if test="${meal == null}">
    <h2>Add New Meal</h2>
</c:if>

<c:if test="${meal != null}">
    <h2>Edit Meal</h2>
</c:if>

<c:if test="${meal != null}">
    <form action="update" method="post">
</c:if>

<c:if test="${meal == null}">
    <form action="insert" method="post">
</c:if>
    <c:if test="${meal != null}">
        <input type="hidden" name="id" value="<c:out value='${meal.id}' />" />
    </c:if>
    <div class="group-input">
        <label for="localDateTime">DateTime:</label>
        <input type="datetime-local" id="localDateTime" value="<c:out value='${meal.dateTime}' />" name="localDateTime">
    </div>

    <div class="group-input">
        <label for="description">Description:</label>
        <input type="text" id="description" value="<c:out value='${meal.description}' />" name="description">
    </div>

    <div class="group-input">
        <label for="calories">Calories:</label>
        <input type="number" id="calories" value="<c:out value='${meal.calories}' />" name="calories">
    </div>

    <div>
        <button type="submit">Save</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </div>

</form>
</body>
</html>
