package br.com.fiap.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConnectionFactory {

    private static final String DB_RELATIVE = "src/main/java/br/com/fiap/database.db";
    private static final String URL;

    static {
        Path path = Paths.get(System.getProperty("user.dir")).resolve(DB_RELATIVE);
        URL = "jdbc:sqlite:" + path.toAbsolutePath() + "?busy_timeout=5000";
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);

        try (var stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode = WAL");
            stmt.execute("PRAGMA synchronous = NORMAL");
        }

        return conn;
    }
}
