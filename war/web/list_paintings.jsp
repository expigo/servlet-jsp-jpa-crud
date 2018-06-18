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
            <h2>Painting's list</h2>
        </div>
        <div class="section__table" >


            <table>
                <tr>
                    <th>Title</th>
                    <th>Painter</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="tempPainting" items="${PAINTING_LIST}" >
                    <!-- sets up a link for each painting -->
                    <c:url var="tempLink" value="PaintingControllerServlet">
                        <c:param name="action" value="LOAD"></c:param>
                        <c:param name="paintingId" value="${tempPainting.id}" />
                    </c:url> 
                    <c:url var="tempLinkDelete" value="PaintingControllerServlet">
                        <c:param name="action" value="DEL"></c:param>
                        <c:param name="paintingId" value="${tempPainting.id}" />
                    </c:url> 
                    <tr>
                        <td>${tempPainting.title}</td>
                        <td>${tempPainting.painter}</td>
                        <td><a href="${tempLink}">Update</a> | <a href="${tempLinkDelete}">Delete</a></td>
                    </tr>

                </c:forEach>

            </table>
            <div class="menu">  

                <%-- <c:choose> 
                     <c:when test = "${!empty paintings}">
                         <a href="add-painting-form.jsp" class="btn btn--white">Add Painting</a>
                     </c:when>
                     <c:otherwise>
                         <div>Add painter first</div>
                     </c:otherwise>
                 </c:choose> 
                --%>

                <c:set var="paintings" value="<%= session.getAttribute("PAINTER_LIST")%>" />
                <c:if test = "${!empty paintings}">
                    <a href="add-painting-form.jsp" class="btn btn--white">Add Painting</a>
                </c:if>
                
                <a href="PainterControllerServlet" class="btn btn--white">List Painters</a>
            </div>

            <div class="section__statistics">
                <table>
                    <tr>
                        <th>Add</th>
                        <th>Delete</th>
                        <th>Update</th>
                    </tr>
                    <tr>
                        <td>${cookie.aCookie.value}</td>
                        <td>${cookie.dCookie.value}</td>
                        <td>${cookie.uCookie.value}</td>

                    </tr>
                </table>
            </div>
            <!--${paintingFound}-->
            <br>
            <!--${PAINTING_LIST}-->

        </div>  
    </body>
</html>
