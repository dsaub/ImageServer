<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="me.elordenador.imageserver.Generator" %>

<% String CSRF_TOKEN = Generator.generateCSRF(); %>


<!DOCTYPE html>
<html>
    <head>
        <title>Pagina principal</title>
        <meta charset="utf-8">
        <meta name="author" content="Daniel Sánchez Úbeda">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <link href="CSS/main.css" rel="stylesheet">
        <meta name="viewport" content="width=device-width, initial-scale=1">


    </head>
    <body style="">
        <header id="title" class="container-fluid" style="background-color: lightblue; color: black">Upload Server</header>

        <main>
            <section class="container-sm">
                <h1>Comprobar TOKEN</h1>
                <form action="api/v1/verifyToken" method="POST">
                    <%= CSRF_TOKEN %>
                    <div class="mb-3">
                        <label for="token" class="form-label">Token</label>
                        <input type="text" class="form-control" id="token">
                        <div id="tokenHelp" class="form-text">El token a comprobar</div>
                    </div>
                    <input type="submit" class="btn btn-primary" />
                    <input type="reset" class="btn btn-danger"/>

                </form>
            </section>
            <section>
                <h1>Subir Imagen</h1>
                <form action="api/v1/uploadImage" method="POST" enctype="multipart/form-data">
                    <%= CSRF_TOKEN %>
                    <label for="file">Select image:</label>
                    <input type="file" name="file" id="file" required><br>
                    <label for="file_name">Nombre del archivo:</label>
                    <input type="text" name="file_name" id="file_name" required><br>
                    <label for="file_description">File description:</label>
                    <input type="text" name="file_description" id="file_description" required><br>
                    <label for="token">Token:</label>
                    <input type="password" name="token" id="token1"><br>
                    <input type="submit" value="Upload">
                    <input type="reset" value="Reset">
                </form>
            </section>
        </main>


        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js" integrity="sha384-IQsoLXl5PILFhosVNubq5LC7Qb9DXgDA9i+tQ8Zj3iwWAwPtgFTxbJ8NT4GN1R8p" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js" integrity="sha384-cVKIPhGWiC2Al4u+LWgxfKTRIcfu0JTxR+EQDz/bgldoEyl4H0zUF0QKbrJ0EcQF" crossorigin="anonymous"></script>


    </body>
</html>