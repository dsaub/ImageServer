package me.elordenador.imageserver;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Generator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TOKEN_LENGTH = 32;
    private static final SecureRandom random = new SecureRandom();

    public static String generateCustomToken() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            token.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return token.toString();
    }

    public static String generateCSRF() throws ClassNotFoundException {
        DBCredentials.init();
        DBCredentials.regenerateJDBC();
        String code = generateCustomToken();
        StringBuilder builder = new StringBuilder();
        builder.append("<input type=\"hidden\" name=\"csrf_token\" id=\"csrf_token\" value=\"");
        builder.append(code);
        builder.append("\" />");
        String string = builder.toString();
        try (Connection conn = DriverManager.getConnection(DBCredentials.getJDBC(), DBCredentials.getDBUser(), DBCredentials.getDBPassword())) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO csrf_token (token) VALUES (?)");
            stmt.setString(1, code);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return string;


    }
}
