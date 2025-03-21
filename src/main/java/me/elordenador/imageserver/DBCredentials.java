package me.elordenador.imageserver;


public class DBCredentials {
    private static String DB_HOST = "localhost";
    private static int DB_PORT = 3306;

    private static String DB_NAME = "dispositivos";
    private static String DB_USER, DB_PASS;

    private static String JDBC_STRING = "";



    public static String getDBUser() {
        return DB_USER;
    }
    public static String getDBPassword() {
        return DB_PASS;
    }

    public static String getJDBC() {
        return JDBC_STRING;
    }


    public static void init() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        DB_HOST = System.getenv("DB_HOST");
        try {
            DB_PORT = Integer.parseInt(System.getenv("DB_HOST"));
        } catch (NumberFormatException e) {
            DB_PORT = 3306;
        }
        DB_NAME = System.getenv("DB_NAME");
        DB_USER = System.getenv("DB_USER");
        DB_PASS = System.getenv("DB_PASS");
        regenerateJDBC();
    }

    public static void regenerateJDBC() {
        JDBC_STRING = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    }

}