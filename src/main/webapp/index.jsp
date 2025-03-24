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
        <% String CSRF_TOKEN = Generator.generateCSRF(); %>
        <div id="title">Upload Server</div>
        <p>This server will serve the ability to open images on my DB.</p>

        <h1>Check TOKEN</h1>
        <form action="api/v1/verifyToken" method="POST">
            <%= CSRF_TOKEN %>
            <label for="token">Token:</label>
            <input type="text" name="token" id="token"><br>
            <input type="submit" />
            <input type="reset" />

        </form>


        <h1>Upload Image</h1>
        <form action="api/v1/uploadImage" method="POST" enctype="multipart/form-data">
            <%= CSRF_TOKEN %>
            <label for="file">Select image:</label>
            <input type="file" name="file" id="file" required><br>
            <label for="file_name">Nombre del archivo:</label>
            <input type="text" name="file_name" id="file_name" required><br>
            <label for="file_description">File description:</label>
            <input type="text" name="file_description" id="file_description" required><br>
            <label for="token">Token:</label>
            <input type="password" name="token" id="token"><br>
            <input type="submit" value="Upload">
            <input type="reset" value="Reset">
        </form>
    </body>
</html>