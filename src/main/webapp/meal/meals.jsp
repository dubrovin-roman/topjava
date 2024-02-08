<%--
  Created by IntelliJ IDEA.
  User: dubro
  Date: 08.02.2024
  Time: 11:18
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
    <style>
        table {
            border-collapse: collapse;
            border: 2px solid rgb(140 140 140);
            font-family: sans-serif;
            font-size: 1rem;
            letter-spacing: 1px;
        }

        caption {
            caption-side: bottom;
            padding: 10px;
            font-weight: bold;
        }

        thead,
        tfoot {
            background-color: rgb(228 240 245);
        }

        th,
        td {
            border: 1px solid rgb(160 160 160);
            padding: 8px 10px;
        }

        td:last-of-type {
            text-align: center;
        }

        tbody > tr:nth-of-type(even) {
            background-color: rgb(237 238 242);
        }

        tfoot th {
            text-align: right;
        }

        tfoot td {
            font-weight: bold;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h3><a href="meals/new">Add meal</a></h3>
<table>
    <thead>
    <tr>
        <th scope="col">Date</th>
        <th scope="col">Description</th>
        <th scope="col">Calories</th>
        <th scope="col">Update</th>
        <th scope="col">Delete</th>
    </tr>
    </thead>
    <c:forEach var="meal" items="${meals}">
        <c:set var="excess" value="${meal.isExcess()}"/>
        <fmt:parseDate value="${meal.getDateTime()}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
        <c:if test="${excess == true}">
            <tr style="color: red">
                <td>
                    <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ parsedDateTime }" />
                </td>
                <td>
                    <c:out value="${meal.getDescription()}"></c:out>
                </td>
                <td>
                    <c:out value="${meal.getCalories()}"></c:out>
                </td>
                <td><a href="meals/edit?id=<c:out value='${meal.getId()}' />">Update</a></td>
                <td><a href="meals/delete?id=<c:out value='${meal.getId()}' />">Delete</a></td>
            </tr>
        </c:if>
        <c:if test="${excess == false}">
            <tr style="color: darkgreen">
                <td>
                    <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}" />
                </td>
                <td >
                    <c:out value="${meal.getDescription()}"></c:out>
                </td>
                <td>
                    <c:out value="${meal.getCalories()}"></c:out>
                </td>
                <td><a href="meals/edit?id=<c:out value='${meal.getId()}' />">Update</a></td>
                <td><a href="meals/delete?id=<c:out value='${meal.getId()}' />">Delete</a></td>
            </tr>
        </c:if>
    </c:forEach>
    <tbody>

    </tbody>
</table>
</body>
</html>
