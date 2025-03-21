module ImageServer {
    requires java.sql;
    requires mysql.connector.j;
    requires com.google.gson;
    requires jakarta.servlet;
    exports me.elordenador.imageserver;
}