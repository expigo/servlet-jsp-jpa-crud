<%-- 
    Document   : add-painting-form
    Created on : 2017-12-09, 23:14:49
    Author     : Kryspin Musiol
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link type="text/css" rel="stylesheet" href="css/list-style.css">
        <link type="text/css" rel="stylesheet" href="css/form-style.css">

        <title>Add painting</title>
    </head>
    <body>

        <div class="header">
            <h2>Update Painting Form</h2>
        </div>

        <div class="container">

            <form action="PainterControllerServlet" method="POST">

                <input type="hidden" name="action" value="UPDATE">
                <!-- additionally sending painting's id to get proper painting from db-->
                <input type="hidden" name="painterId" value="${THE_PAINTER.id}">

                <table>
                    <tbody>

                        <tr>
                            <td><label>First Name</label></td>
                            <td><input type="text" name="firstName" value="${THE_PAINTER.firstName}"></td>
                        </tr>

                        <tr>
                            <td><label>Last Name</label></td>
                            <td><input type="text" name="lastName" value="${THE_PAINTER.lastName}"></td>
                        </tr>
<!--                        <tr>
                            <td><label>Day of birth</label></td>
                            <td><input type="text" name="dayOfBirth" value="${THE_PAINTER.dateOfBirth}"></td>
                        </tr> -->
                        <tr>
                            <td><input type="submit" value="submit" class="btn"></td>
                        </tr>
                    </tbody>
                </table>

            </form>

            <a href="PainterControllerServlet" class="btn btn--white">back</a>

        </div>

    </body>
</html>
