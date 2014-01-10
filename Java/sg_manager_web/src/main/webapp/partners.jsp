<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Partners</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css" />
    </head>
    <body>
        <div class="datagrid">
            <table>
                <thead><tr><th>Partner name</th><th>Action</th><th>Site</th></tr></thead>
                <tbody>
                    <c:forEach items="${partners}" var="partner" varStatus="loopStatus">
                        <tr class="${loopStatus.index % 2 == 0 ? '' : 'alt'}">
                            <td>${partner.name}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/product-add-localization?product=${product.id}" class="add_button">Add »</a>

                            </td>
                            <td>
                                <a href="${partner.uri}" target="_blank">${partner.uri}</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <p></p>
        <form method="POST">
            <fieldset>
                <legend>New author parameters</legend>
                <p> 
                    <label for="name">Name</label>
                    <input name="name" type="text" placeholder="Enter author name" required class="text_input"/>
                    <label class="text_input_validation"></label>
                </p>
                <p> 
                    <label for="uri">Name</label>
                    <input name="uri" type="text" placeholder="Enter site uri" class="text_input" pattern="^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$"/>
                    <label class="text_input_validation"></label>
                </p>
                <p><input class="submit_add" type="submit" value="create"/></p>
            </fieldset>

        </form>
    </body>
</html>