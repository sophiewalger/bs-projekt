package de.bs.hausfix.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection implements IDatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private final Properties properties;

    private DatabaseConnection() {
        this.properties = new Properties();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    @Override
    public IDatabaseConnection openConnection(Properties properties) {
        try {
            String url = properties.getProperty(properties.getProperty("user.name") + ".db.url");
            String user = properties.getProperty(properties.getProperty("user.name") + ".db.user");
            String password = properties.getProperty(properties.getProperty("user.name") + ".db.pw");
            System.out.println(url  +" "  +  " "+user + " "+password);

            if (url == null || user == null || password == null) {
                throw new IllegalStateException("Database properties not found");
            }

            this.connection = DriverManager.getConnection(url, user, password);
            this.properties.putAll(properties);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }

        return this;
    }

    @Override
    public void createAllTables() {
        try {
            String schema = loadSchemaFile();
            try (Statement stmt = connection.createStatement()) {
                for (String sql : schema.split(";")) {
                    if (!sql.trim().isEmpty()) {
                        stmt.execute(sql);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Failed to create tables", e);
        }
    }

    @Override
    public void truncateAllTables() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            stmt.execute("TRUNCATE TABLE readings");
            stmt.execute("TRUNCATE TABLE customers");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to truncate tables", e);
        }
    }

    @Override
    public void removeAllTables() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            stmt.execute("DROP TABLE IF EXISTS readings");
            stmt.execute("DROP TABLE IF EXISTS customers");
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove tables", e);
        }
    }

    @Override
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close database connection", e);
            }
        }
    }

    public Connection getConnection() {
        return connection; // Diese Methode gibt die Verbindung zurück
    }

    private String loadSchemaFile() throws IOException {
        Path schemaPath = Paths.get("src", "main", "resources", "schema.sql");
        return Files.readString(schemaPath);
    }
}