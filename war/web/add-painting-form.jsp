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
            <h2>Add Painting Form</h2>
        </div>

        <div class="container">

            <form action="PaintingControllerServlet" method="POST">

                <input type="hidden" name="action" value="ADD">

                <table>
                    <tbody>
                        <tr>
                            <td><label>Title</label></td>
                            <td><input type="text" name="title"></td>
                        </tr>
                        <tr>
                            <td><label>Painter</label></td>
                            <td>
                                <select name="painter">
                                    <c:forEach var="tempPainter" items="<%= session.getAttribute("PAINTER_LIST")%>">
                                        <option value="${tempPainter.id}">${tempPainter.firstName} ${tempPainter.lastName}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td><input type="submit" value="submit" class="btn"></td>
                        </tr>
                    </tbody>
                </table>

            </form>

            <a href="PaintingControllerServlet" class="btn btn--white">back</a>

        </div>

    </body>
</html>
