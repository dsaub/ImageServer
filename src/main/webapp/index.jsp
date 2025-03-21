<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="me.elordenador.imageserver.Generator" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Pagina principal</title>
        <meta charset="utf-8">
        <meta name="author" content="Daniel Sánchez Úbeda">
        <link rel="stylesheet" href="CSS/main.css">
    </head>
    <body>
        <div id="title">Upload Server</div>
        <p>This server will serve the ability to open images on my DB.</p>


        <form action="api/v1/verifyToken" method="POST">
            <%= Generator.generateCSRF() %>
            <label for="token">Token:</label>
            <input type="text" name="token" id="token"><br>
            <input type="submit" />
            <input type="reset" />

        </form>
    </body>
</html>