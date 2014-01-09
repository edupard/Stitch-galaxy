<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page import="com.stitchgalaxy.sg_manager_web.data.Category"%>
<%@page import="com.stitchgalaxy.sg_manager_web.data.Design"%>
<%@page import="com.stitchgalaxy.sg_manager_web.data.ProductLocalization"%>
<%@page import="org.joda.time.format.DateTimeFormatter"%>
<%@page import="org.joda.time.format.ISODateTimeFormat"%>
<%@page import="com.stitchgalaxy.sg_manager_web.data.Product"%>
<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Edit product</title>

        <link rel="stylesheet" href="./css/colorpicker.css" type="text/css" />

        <script src="./scripts/jquery.js"></script>
        <script src="./scripts/jquery.validate.min.js"></script>


        <script type="text/javascript" src="./js/colorpicker.js"></script>
        <script type="text/javascript" src="./js/eye.js"></script>
        <script type="text/javascript" src="./js/utils.js"></script>
        <script type="text/javascript" src="./js/layout.js?ver=1.0.2"></script>


        <script>

            $().ready(function() {

                $("#productForm").validate({
                    rules: {
                        sales:
                                {
                                    digits: true
                                },
                        rating:
                                {
                                    digits: true
                                },
                        rates:
                                {
                                    digits: true
                                },
                        author_uri:
                                {
                                    url: true
                                },
                        translator_uri:
                                {
                                    url: true
                                }
                    },
                    messages: {
                        date: "Please enter valid publication date",
                        price: "Please enter valid price in USD",
                        name: "Please enter design name",
                        sales:
                                {
                                    digits: "Please enter valid number"
                                },
                        rating: {
                            digits: "Please enter valid number"
                        },
                        rates: {
                            digits: "Please enter valid number"
                        },
                        author_uri:
                                {
                                    url: "Please enter valid uri"
                                },
                        translator_uri:
                                {
                                    url: "Please enter valid uri"
                                },
                        complexity:
                                {
                                    number: "Please enter valid number"
                                }
                    }
                });

                $("#edit_locale").click(function(e) {
                    e.preventDefault();
                    var locale = $("#locale").val();
                    if (locale)
                    {

                        var href = $(this).attr("href");
                        href += locale;
                        window.location = href;
                    }
                    else
                    {
                        alert("Locale should not be empty");
                    }
                });
            });
        </script>

        <style type="text/css">
            #productForm { width: 100%; }
            #productForm label.error {
                margin-left: 10px;
                width: auto;
                display: inline;
            }
            .design_thumbnail_placeholder { height: 100px; width: 100px; overflow: hidden; }
            .design_thumbnail { max-height: 100px; max-width: 100px; width: auto; height: auto; }
            
            .image_placeholder { height: 100px; width: 100px; }
            .image { max-height: 100px; max-width: 100px; width: auto; height: auto; }
        </style>

    </head>

    <body>
        <%
            Product product = (Product) request.getAttribute("product");
            DateTimeFormatter dtf = ISODateTimeFormat.date();
        %>
        <p><h4>Manage product parameters</h4></p>
        <form id="productForm" action="/sg_manager_web/edit_product?product=<% out.print(product.getId()); %>" method="POST">
            <fieldset>
                <legend>product parameters</legend>
                <p>
                    
                    <label for="id">ID</label>
                    <input id="id" name="id" type="text" required readonly value="<% out.print(product.getId()); %>"/>
                </p>
                <p>
                    <label for="blocked">Blocked</label>
                    <%
                        if (product.isBlocked()) {
                    %>
                    <input id="blocked" name="blocked" type="checkbox" checked/>
                    <%
                    } else {
                    %>
                    <input id="blocked" name="blocked" type="checkbox"/>
                    <%
                        }
                    %>
                </p>
                <p>
                    <label for="id">Name</label>
                    <input id="name" name="name" type="text" required value="<% out.print(product.getName()); %>"/>
                </p>
                <p>
                    <label for="description">Description</label>
                </p>
                <p>
                    <textarea id="description" name="description" cols="50" rows="3"><% out.print(product.getDescription() == null ? "" : product.getDescription()); %></textarea>
                </p>
                <p>
                    <label for="date">Publication date</label>
                    <input id="date" name="date" type="dateISO" required value="<% out.print(product.getDate().toString(dtf)); %>"/>
                </p>
                <p>
                    <label for="price">Price in USD</label>
                    <input id="price" name="price" type="number" required value="<% out.print(product.getPriceUsd()); %>"/>
                </p>

                <p>
                    <%
                        if (product.getAuthor() != null) {
                    %>
                    Author:
                    <b><i><% out.print(product.getAuthor().getName()); %></i></b>
                    <a href="<% out.print(product.getAuthor().getUri()); %>" target="_blank">site</a>
                    <br/>
                    <a href="/sg_manager_web/remove_author?product=<% out.print(product.getId());%>">Remove</a>
                    <%
                        }
                    %>
                    <a href="/sg_manager_web/select_author?product=<% out.print(product.getId()); %>">Set author</a>
                </p>

                <p>
                    <%
                        if (product.getTranslator() != null) {
                    %>
                    Translator:
                    <b><i><% out.print(product.getTranslator().getName()); %></i></b>
                    <a href="<% out.print(product.getTranslator().getUri()); %>" target="_blank">site</a>
                    <br/>
                    <a href="/sg_manager_web/remove_translator?product=<% out.print(product.getId());%>">Remove</a>
                    <%
                        }
                    %>
                    <a href="/sg_manager_web/select_translator?product=<% out.print(product.getId());%>">Set translator</a>
                </p>

                <p>
                    <label for="sales">Sales</label>
                    <input id="sales" name="sales" type="text" value="<% out.print(product.getSales() == null ? "" : product.getSales()); %>"/>
                </p>
                <p>
                    <label for="rating">Total rating (sum of all user rates)</label>
                    <input id="rating" name="rating" type="text" value="<% out.print(product.getRating() == null ? "" : product.getRating()); %>"/>
                </p>
                <p>
                    <label for="rates">Rates</label>
                    <input id="rates" name="rates" type="text" value="<% out.print(product.getRates() == null ? "" : product.getRates()); %>"/>
                </p>
                <p>
                    <label for="tags">Tags <br/><b>Note: place each tag on the new line</b></label>
                </p>
                <p>
                    <textarea id="tags" name="tags" cols="50" rows="3"><% out.print(product.getTags() == null ? "" : product.getTags()); %></textarea>
                </p>
                <p>
                    <label for="colorpickerField1">Average color</label>
                    <input type="text" maxlength="6" size="6" id="colorpickerField1" value="<% out.print(product.getAvgColor() == null ? "" : String.format("%02X%02X%02X", product.getAvgColor().getRed(), product.getAvgColor().getGreen(), product.getAvgColor().getBlue())); %>" />
                </p>
                <p>
                    <label for="complexity">Complexity</label>
                    <input id="complexity" name="complexity" type="number" value="<% out.print(product.getComplexity() == null ? "" : product.getComplexity()); %>"/>
                </p>
                <p>
                    <input class="submit" type="submit" value="Update"/>
                </p>
            </fieldset>
        </form>
        <hr/>
        <p><h4>Manage localizations</h4></p>
        <%
            if (product.getLocalizations().size() > 0) {
        %>
        <p>
            <table border="1">
                <tr><td><b>Localization</b></td><td><b>Action</b></td></tr>
                <%
                    for (ProductLocalization localization : product.getLocalizations()) {
                %>
                <tr>
                    <td>
                        <a href="/sg_manager_web/edit_localization?product=<% out.print(product.getId());%>&locale=<% out.print(localization.getLocale());%>"><% out.print(localization.getLocale());%></a>
                    </td>
                    <td>
                        <a href="/sg_manager_web/remove_localization?product=<% out.print(product.getId());%>&locale=<% out.print(localization.getLocale());%>">Remove</a>
                    </td>
                </tr>
                <%
                    }
                %>
            </table>
        </p>
        <%
            }
        %>
        <p>
            <label for="locale">Locale</label>
            <input id="locale" type="text"><a style="margin-left:10px;" href="/sg_manager_web/edit_localization?product=<% out.print(product.getId());%>&locale=" id="edit_locale">Add or edit</a>
        </p>
        <hr/>
        <p><h4>Manage designs</h4></p>
        <%
            if (product.getDesigns().size() > 0) {
        %>
        <p>
            <table border="1">
                <tr><td><b>Design</b></td><td><b>Thumbnail</b></td><td><b>Action</b></td></tr>
                <%
                    for (Design design : product.getDesigns()) {
                %>
                <tr>
                    <td>
                        <a href="/sg_manager_web/edit_design?product=<% out.print(product.getId());%>&design=<% out.print(design.getId());%>"><% out.print(design.getPartId());%></a>
                    </td>
                    <td>
                        <div class="design_thumbnail_placeholder">
                            <%
                                if (design.getThumbnailUri() != null) {
                            %>
                            <img src="<% out.print(design.getThumbnailUri());%>" border="1" class="design_thumbnail"/>
                            <%
                                }
                            %>
                        </div>
                    </td>
                    <td>
                       <a href="/sg_manager_web/delete_design?product=<% out.print(product.getId());%>&design=<% out.print(design.getId());%>">Delete</a> 
                    </td>
                </tr>
                <%
                    }
                %>
            </table>
        </p>
        <%
            }
        %>
        <p>
            <a style="margin-left:10px;" href="/sg_manager_web/add_design?product=<% out.print(product.getId());%>" id="add_design">Add design</a>
        </p>
        <hr/>
        <p><h4>Manage categories</h4></p>
        <%
            if (product.getCategories().size() > 0) {
        %>
        <p>
            <table border="1">
                <tr><td><b>Category</b></td><td><b>Action</b></td></tr>
                <%
                    for (Category category : product.getCategories()) {
                %>
                <tr>
                    <td>
                        <a href="/sg_manager_web/browse_category?category=<% out.print(category.getId());%>"><% out.print(category.getName());%></a>
                    </td>
                    <td>
                       <a href="/sg_manager_web/delete_category?product=<% out.print(product.getId());%>&category=<% out.print(category.getId());%>">Delete</a> 
                    </td>
                </tr>
                <%
                    }
                %>
            </table>
        </p>
        <%
            }
        %>
        <p>
            <a style="margin-left:10px;" href="/sg_manager_web/add_category?product=<% out.print(product.getId());%>" id="add_category">Add category</a>
        </p>
        <hr/>
        <p>
            <h4>Manage files</h4>
        </p>
        <p>Prototype:</p>
        <%
            if (product.getPrototypeUri() != null) {
        %>
        <div class="image_placeholder">
            <img src="<% out.print(product.getPrototypeUri());%>" border="1" class="image"/>
        </div>
        <%
            }
        %>
        <form action="/sg_manager_web/upload_prototype?product=<% out.print(product.getId()); %>" method="post" enctype="multipart/form-data">
            <input type="file" name="file" />
            <input type="submit" />
        </form>
        <p>Thumbnail:</p>
        <%
            if (product.getThumbnailUri()!= null) {
        %>
        <div class="image_placeholder">
            <img src="<% out.print(product.getThumbnailUri());%>" border="1" class="image"/>
        </div>
        <%
            }
        %>
        <form action="upload_thumbnail" method="post" enctype="multipart/form-data">
            <input type="file" name="file" />
            <input type="submit" />
        </form>
        <p>Large image:</p>
        <%
            if (product.getLargeImageUri()!= null) {
        %>
        <div class="image_placeholder">
            <img src="<% out.print(product.getLargeImageUri());%>" border="1" class="image"/>
        </div>
        <%
            }
        %>
        <form action="upload_large_image" method="post" enctype="multipart/form-data">
            <input type="file" name="file" />
            <input type="submit" />
        </form>
        <p>Completed image:</p>
        <%
            if (product.getCompleteImageUri()!= null) {
        %>
        <div class="image_placeholder">
            <img src="<% out.print(product.getCompleteImageUri());%>" border="1" class="image"/>
        </div>
        <%
            }
        %>
        <form action="upload_complete_image" method="post" enctype="multipart/form-data">
            <input type="file" name="file" />
            <input type="submit" />
        </form>
    </body>
</html>