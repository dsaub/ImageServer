package me.elordenador.imageserver;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

@WebServlet(name = "GetImageServlet", value = "/api/v1/getImage")
public class GetImageServlet extends HttpServlet {

    private File UPLOAD_FOLDER = new File("images");
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            DBCredentials.init();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        DBCredentials.regenerateJDBC();
        int imageID = Integer.parseInt(request.getParameter("id"));
        try (Connection conn = DriverManager.getConnection(DBCredentials.getJDBC(), DBCredentials.getDBUser(), DBCredentials.getDBPassword())) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, formato_imagen FROM image WHERE id = ?");
            stmt.setInt(1, imageID);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                String imageFormat = rs.getString("formato_imagen");
                response.setContentType("image/" + imageFormat);
                response.setHeader("Cache-Control", "no-cache");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);

                File imageFile = new File(UPLOAD_FOLDER, imageID + "." + imageFormat);

                if (!imageFile.exists()) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                try (FileInputStream fis = new FileInputStream(imageFile);
                     OutputStream os = response.getOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
