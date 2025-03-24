package me.elordenador.imageserver;

import java.sql.*;

public class Verificator {


    public static void verifyCSRF(String token) throws Exception {
        DBCredentials.init();
        DBCredentials.regenerateJDBC();
        try (Connection conn = DriverManager.getConnection(DBCredentials.getJDBC(), DBCredentials.getDBUser(), DBCredentials.getDBPassword())) {
            PreparedStatement stmt = conn.prepareStatement("SELECT token FROM csrf_token WHERE token=?");
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            if (!rs.getString("token").equals(token)) {
                throw new Exception("CSRF Verification failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void verifyToken(String token) throws Exception {
        DBCredentials.init();
        DBCredentials.regenerateJDBC();
        try (Connection conn = DriverManager.getConnection(DBCredentials.getJDBC(), DBCredentials.getDBUser(), DBCredentials.getDBPassword())) {
            PreparedStatement stmt = conn.prepareStatement("SELECT token FROM token WHERE token=?");
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            if (!rs.getString("token").equals(token)) {
                throw new Exception("CSRF Verification failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
