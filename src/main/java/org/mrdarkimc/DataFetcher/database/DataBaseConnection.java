package org.mrdarkimc.DataFetcher.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

public class DataBaseConnection {
    private String host;
    private String port;
    private String db;
    private String user;
    private String pass;
    private final String url;

    public DataBaseConnection(String host, String port, String db, String user, String pass) {
        this.host = host == null ? "localhost" : host;
        this.port = port == null ? "5432" : port;
        this.db = db == null ? "postgres" : db;
        this.user = user == null ? "postgres" : user;
        this.pass = pass == null ? "root" : pass;
        this.url = "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.db;
    }

    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
    public void showAbout(){
        System.out.println("Имя хоста: " + host);
        System.out.println("Порт: " + port);
        System.out.println("Имя базы данных: " + db);
        System.out.println("Имя пользователя: " + user);
        System.out.println("Пароль: " + pass);
    }
}
