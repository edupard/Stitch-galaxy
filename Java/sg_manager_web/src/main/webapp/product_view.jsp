<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.stitchgalaxy.sg_manager_web.data.Product"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>View product</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sg.css" type="text/css" />
    </head>
    <body>
        <%
            Product product = (Product) request.getAttribute("product");
        %>
        <div class="datagrid">
            <table>
                <thead><tr><th>Parameter</th><th>Value</th></tr></thead>
                <tbody>
                    <tr>
                        <td>
                            Identifier
                        </td>
                        <td>${product.id}</td>
                    </tr>
                    <tr class="alt">
                        <td>
                            Name
                        </td>
                        <td>${product.name}</td>
                    </tr>
                    <tr>
                        <td>
                            Price
                        </td>
                        <td>${product.priceUsd}</td>
                    </tr>
                    <tr class="alt">
                        <td>
                            Publication date
                        </td>
                        <td>${product.date}</td>
                    </tr>
                    <tr>
                        <td>
                            Blocked
                        </td>
                        <td>${product.isBlocked()}</td>
                    </tr>
                    <tr class="alt">
                        <td>
                            Description
                        </td>
                        <td><div style="width: 100%; height:100%; overflow: auto;">${fn:replace(product.description, newLineChar, "<br/>")}</div></td>
                    </tr>
                    <tr>
                        <td>
                            Sales
                        </td>
                        <td>${product.sales}</td>
                    </tr>
                    <tr class="alt">
                        <td>
                            Rating (sum of all user rates)
                        </td>
                        <td>${product.rating}</td>
                    </tr>
                    <tr>
                        <td>
                            Rates amount
                        </td>
                        <td>${product.rates}</td>
                    </tr>
                    <tr class="alt">
                        <td>
                            Average rating
                        </td>
                        <td>${product.rates != null && product.rating != null ? product.rating / product.rates : 0}</td>
                    </tr>
                    <tr>
                        <td>
                            Complexity
                        </td>
                        <td>${product.complexity}</td>
                    </tr>
                    <tr class="alt">
                        <td>
                            Average color
                        </td>
                        <%String sColor = product.getAvgColor() == null ? "" : String.format("#%02X%02X%02X", product.getAvgColor().getRed(), product.getAvgColor().getGreen(), product.getAvgColor().getBlue());%>
                        <td style="background-color: <% out.print(sColor); %>"><% out.print(sColor);%></td>
                    </tr>
                    <tr>
                        <td>
                            Tags
                        </td>
                        <td><div style="width: 100%; height:100%; overflow: auto;">${fn:replace(product.tags, newLineChar, "<br/>")}</div></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <br/>
        <a href="${pageContext.request.contextPath}/product_edit?product=${product.id}" class="edit_button">Edit design »</a>
        <br/>
        <c:choose>
            <c:when test="${product.author != null}">
                <div class="datagrid">
                    <table>
                        <thead><tr><th>Author</th><th>Site</th></tr></thead>
                        <tbody>
                            <tr>
                                <td>
                                    ${product.author.name}
                                </td>
                                <td><a href="${product.author.uri}" target="_blank">${product.author.uri}</a></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <br/>
                <a href="${pageContext.request.contextPath}/product_remove_author?product=${product.id}" class="delete_button">Delete author »</a>
                <a href="${pageContext.request.contextPath}/product_set_author?product=${product.id}" class="edit_button">Change author »</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/product_set_author?product=${product.id}" class="add_button">Set author »</a>
            </c:otherwise>
        </c:choose>
        <br/>
        <c:choose>
            <c:when test="${product.translator != null}">
                <div class="datagrid">
                    <table>
                        <thead><tr><th>Translator</th><th>Site</th></tr></thead>
                        <tbody>
                            <tr>
                                <td>
                                    ${product.translator.name}
                                </td>
                                <td><a href="${product.author.uri}" target="_blank">${product.translator.uri}</a></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <br/>
                <a href="${pageContext.request.contextPath}/product_remove_translator?product=${product.id}" class="delete_button">Delete translator »</a>
                <a href="${pageContext.request.contextPath}/product_set_translator?product=${product.id}" class="edit_button">Change translator »</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/product_set_translator?product=${product.id}" class="add_button">Set translator »</a>
            </c:otherwise>
        </c:choose>
        <br/>
        <%List<String> a = new LinkedList<String>(); a.add("a");%>
        <div class="datagrid">
            <table>
                <thead><tr><th>Locale</th><th>Name</th><th>Description</th><th>Tags</th></tr></thead>
                <tbody>
                    <c:forEach var="localization" items="${product.localizations}" varStatus="loopStatus">
                        <tr class="${loopStatus.index % 2 == 0 ? '' : 'alt'}">
                            <td>
                                ${localization.locale}<a href="${pageContext.request.contextPath}/product_edit_locale?product=${product.id}&localization=${localization.locale}" class="edit_button">edit »</a>
                            </td>
                            <td>${localization.name}</td>
                            <td>
                                <div style="width: 100%; height:100%; overflow: auto;">${fn:replace(localization.description, newLineChar, "<br/>")}</div>
                            </td>
                            <td>
                                <div style="width: 100%; height:100%; overflow: auto;">${fn:replace(localization.tags, newLineChar, "<br/>")}</div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <br/>
        <a href="${pageContext.request.contextPath}/product_add_localization?product=${product.id}" class="add_button">Add localization »</a>
    </body>
</html>