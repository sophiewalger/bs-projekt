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
import java.util.UUID;

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
            // Hier wird die Verbindung zur spezifischen Datenbank hergestellt
            String url = properties.getProperty("user.name" + ".db.url");
            String user = properties.getProperty("user.name" + ".db.user");
            String password = properties.getProperty("user.name" + ".db.pw");

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
        try (Statement stmt = connection.createStatement()) {
            // Erstelle die Datenbank falls sie nicht existiert
            stmt.execute("CREATE DATABASE IF NOT EXISTS hausfix_db");
            stmt.execute("USE hausfix_db");

            // Erstelle die customers Tabelle mit UUID
            String createCustomersTable = """
            CREATE TABLE IF NOT EXISTS customers (
                id CHAR(36) PRIMARY KEY,
                firstname VARCHAR(50),
                lastname VARCHAR(50),
                birthdate DATE,
                gender VARCHAR(1),
                street VARCHAR(100),
                housenumber VARCHAR(10),
                postcode VARCHAR(10),
                city VARCHAR(50)
            )
            """;
            stmt.execute(createCustomersTable);

            // Erstelle die readings Tabelle mit UUID und Fremdschlüssel zu customers
            String createReadingsTable = """
                    CREATE TABLE IF NOT EXISTS readings (
                        id CHAR(36) PRIMARY KEY,
                        meter_reading VARCHAR(255) NOT NULL, 
                        kind_of_meter VARCHAR(20) NOT NULL, 
                        meter_count DOUBLE NOT NULL, 
                        reading_date DATE, 
                        substitute BOOLEAN, 
                        comment TEXT, 
                        customer_id CHAR(36), 
                        FOREIGN KEY (customer_id) REFERENCES customers(id)
                    );
            """;
            stmt.execute(createReadingsTable);

        } catch (SQLException e) {
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

    // Methode zum Erstellen einer neuen UUID
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}