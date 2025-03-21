package me.elordenador.imageserver;

import java.io.*;
import java.sql.*;

import com.google.gson.Gson;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/api/v1/verifyToken")
public class HelloServlet extends HttpServlet {
    class Response {
        private boolean verified;
        private String message;

        public Response(boolean status, String message) {
            this.verified = status;
            this.message = message;
        }

        public boolean getVerified() {
            return verified;
        }
        public void setVerified(boolean verified) {
            this.verified = verified;
        }
    }


    private String message;

    public void init() {
        try {
            DBCredentials.init();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        DBCredentials.regenerateJDBC();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        // Hello
        PrintWriter out = response.getWriter();
        Response response1 = new Response(false, "Method does not support GET");
        Gson gson = new Gson();
        String json = gson.toJson(response1);
        out.println(json);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            Verificator.verifyCSRF(request.getParameter("csrf_token"));
        } catch (Exception e) {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.println(e.getMessage());
            return;

        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String token = request.getParameter("token");
        Gson gson = new Gson();
        try (Connection conn = DriverManager.getConnection(DBCredentials.getJDBC(), DBCredentials.getDBUser(), DBCredentials.getDBPassword())) {
            PreparedStatement stmt = conn.prepareStatement("SELECT token FROM token WHERE token = ?");
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            if (rs.getString("token").equals(token)) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.println(gson.toJson(new Response(true, "Token verified")));
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(gson.toJson(new Response(false, "Token verification failed")));
            }
        } catch (SQLException e) {
            if (e.getMessage().equals("Illegal operation on empty result set.")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.println(gson.toJson(new Response(false, "Token verification failed")));
            } else {
                out.println(gson.toJson(new Response(false, e.getMessage())));
            }
        }
    }

    public void destroy() {
    }
}