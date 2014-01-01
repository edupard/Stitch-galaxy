<%-- 
    Document   : home
    Created on : Dec 30, 2013, 12:52:36 PM
    Author     : tarasev
--%>

<%@page contentType="text/html" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>New product</title>

        <link rel="stylesheet" href="./css/cmxform.css" />
        
        <script src="./scripts/jquery.js"></script>
        <script src="./scripts/jquery.validate.min.js"></script>
    
        <script>

$().ready(function() {
    
    $("#productForm").validate({
                messages: {
                        date: "Please enter valid publication date",
                        price: "Please enter valid price in USD",
                        name: "Please enter design name"
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
</style>

    </head>

    <body>
        <form class="cmxform" id="productForm" action="/sg_manager_web/new_product" method="POST">
            <fieldset>
                <legend>Please provide product parameters</legend>
                <p>
                    <label for="id">ID</label>
                    <%
                            StringBuilder sb = new StringBuilder();
    sb.append("<input size=40 id=\"id\" name=\"id\" type=\"text\" required readonly value=\"");
    sb.append(request.getAttribute("id").toString());
    sb.append("\"/>");
    out.println(sb.toString());
                        %>
                </p>
                <p>
                    <label for="name" >Name</label>
                    <input id="name" name="name" type="text" required/>
                </p>
                <p>
                    <label for="date">Publication date (yyyy-mm-dd)</label>
                    <input id="date" name="date" type="dateISO" required/>
                </p>
                <p>
                    <label for="price">Price in USD</label>
                    <input id="price" name="price" type="number" required value="1"/>
                </p>
                <p>
                    <input class="submit" type="submit" value="Create product"/>
                </p>
            </fieldset>
        </form>
    </body>
</html>
