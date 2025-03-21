package me.elordenador.imageserver;


import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet(name = "UploadImageServlet", value = "/api/v1/uploadImage")
public class UploadImageServlet {
    private class Status {
        private int status;
        private String message;
        private int imageID;
        public Status(int status, String message, int imageID) {
            this.status = status;
        }
    }
    private File UPLOAD_FOLDER = new File("images");
    public UploadImageServlet() {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        try {
            Verificator.verifyCSRF(request.getParameter("csrf_token"));
        } catch (Exception e) {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.println(e.getMessage());
            return;

        }

        if (UPLOAD_FOLDER.exists() && !UPLOAD_FOLDER.isDirectory()) {
            UPLOAD_FOLDER.delete();
            UPLOAD_FOLDER = new File(UPLOAD_FOLDER.getAbsolutePath());
        }

        if (!UPLOAD_FOLDER.exists()) {
            UPLOAD_FOLDER.mkdir();
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            Part filePart = request.getPart("file");
            String file_name = request.getParameter("file_name");
            String file_description = request.getParameter("file_description");
            String file_format = getFileName(filePart).split("\\.")[getFileName(filePart).split("\\.").length - 1];
            Connection connection = DriverManager.getConnection(DBCredentials.getJDBC(), DBCredentials.getDBUser(), DBCredentials.getDBPassword());
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO image (nombre_imagen, descripcion_imagen, formato_imagen) VALUES (?, ?, ?)");
            stmt.setString(1, file_name);
            stmt.setString(2, file_description);
            stmt.setString(3, file_format);

            stmt.executeUpdate();

            stmt = connection.prepareStatement("SELECT id FROM image ORDER BY id DESC LIMIT 1");
            ResultSet rs = stmt.executeQuery();

            rs.next();

            int id = rs.getInt("id");
            stmt.close();
            connection.close();
            File file = new File(UPLOAD_FOLDER, id+"."+file_format);
            try (InputStream fileContent = filePart.getInputStream(); FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileContent.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            response.setStatus(HttpServletResponse.SC_OK);
            Status status = new Status(0, "File uploaded", id);
            Gson gson = new Gson();
            String json = gson.toJson(status);
            out.println(json);
        } catch (Exception e) {
            response.setContentType("application/json");
            Status status = new Status(1, e.getMessage(), 0);
            Gson gson = new Gson();
            String json = gson.toJson(status);
            out.println(json);
        }


    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String cd : contentDisposition.split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
