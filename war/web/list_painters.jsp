<%-- 
    Document   : list_paintings
    Created on : 2017-12-09, 16:03:00
    Author     : Kryspin Musiol
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <link type="text/css" rel="stylesheet" href="css/list-style.css">
    </head>
    <body>


        <div class="header">
            <h2>Painter's list</h2>
        </div>
        <div class="section__table" >


            <table>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Age</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="tempPainter" items="${PAINTER_LIST}" >

                    <!-- sets up a link for each painting -->
                    <c:url var="tempLink" value="PainterControllerServlet">
                        <c:param name="action" value="LOAD"></c:param>
                        <c:param name="painterId" value="${tempPainter.id}" />
                    </c:url>
                    <c:url var="tempLinkDelete" value="PainterControllerServlet">
                        <c:param name="action" value="DEL"></c:param>
                        <c:param name="painterId" value="${tempPainter.id}" />
                    </c:url>
                    <tr>
                        <td>${tempPainter.firstName}</td>
                        <td>${tempPainter.lastName}</td>
                        <td>${tempPainter.age}</td>
                        <td> <a href="${tempLink}">Update</a> | <a href="${tempLinkDelete}">Delete</a></td>

                    </tr>

                </c:forEach>

            </table>
            <div class="menu">
                <!--<a href="add-painting-form.jsp?painterList=${PAINTER_LIST}" class="btn btn--white">Add Painter</a> -->
                <a href="add-painter-form.jsp" class="btn btn--white">Add Painter</a>
                <a href="PaintingControllerServlet" class="btn btn--white">List Paintings</a>
            </div>
        </div>  

        <a href="PaintingControllerServlet" class="btn btn--white">back</a>
        
        
        <!--Painter to be updated: ${PAINTER_TO_UPDATE} -->
        
                    <div class="section__statistics">
                <table>
                    <tr>
                        <th>Add</th>
                        <th>Delete</th>
                        <th>Update</th>
                    </tr>
                    <tr>
                        <td>${cookie.addCookie.value}</td>
                        <td>${cookie.delCookie.value}</td>
                        <td>${cookie.updateCookie.value}</td>

                    </tr>
                </table>
            </div>
    </body>
</html>
