package de.bs.hausfix.dao;

import de.bs.hausfix.db.DatabaseConnection;
import de.bs.hausfix.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class ReadingDAO {
    private static ReadingDAO instance; // Singleton-Instanz
    private final Connection connection;

    // Privater Konstruktor
    private ReadingDAO() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Properties properties = loadDatabaseProperties(); // Methode zum Laden der DB-Eigenschaften
        dbConnection.openConnection(properties); // Verbindung öffnen
        this.connection = dbConnection.getConnection(); // Verbindung abrufen
    }

    // Statische Methode zur Rückgabe der Singleton-Instanz
    public static ReadingDAO getInstance() {
        if (instance == null) {
            instance = new ReadingDAO();
        }
        return instance;
    }

    public void create(IReading reading) {
        String sql = "INSERT INTO readings (id, meter_reading, kind_of_meter, meter_count, reading_date, substitute, comment, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (reading.getId() == null) {
                reading.setId(UUID.randomUUID());
            }

            stmt.setString(1, reading.getId().toString());
            stmt.setString(2, reading.getMeterId()); // Assuming meter_reading corresponds to meter_count
            stmt.setString(3, reading.getKindOfMeter().toString());
            stmt.setDouble(4, reading.getMeterCount());
            stmt.setDate(  5, java.sql.Date.valueOf(reading.getDateOfReading()));
            stmt.setBoolean(6, reading.getSubstitute());
            stmt.setString(7, reading.getComment());
            stmt.setString(8, reading.getCustomer() != null ? reading.getCustomer().getId().toString() : null);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create reading", e);
        }
    }

    public IReading read(UUID id) {
        String sql = "SELECT * FROM readings WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Erstellen und Befüllen des Reading-Objekts direkt hier
                Reading reading = new Reading();
                reading.setId(UUID.fromString(rs.getString("id")));
                reading.setMeterId(rs.getString("meter_reading")); // Assuming meter_reading corresponds to meterId
                reading.setKindOfMeter(KindOfMeter.valueOf(rs.getString("kind_of_meter")));
                reading.setMeterCount(rs.getDouble("meter_count"));
                reading.setDateOfReading(rs.getDate("reading_date").toLocalDate());
                reading.setSubstitute(rs.getBoolean("substitute"));
                reading.setComment(rs.getString("comment"));

                String customerId = rs.getString("customer_id");
                if (customerId != null) {
                    reading.setCustomer(CustomerDAO.getInstance().read(UUID.fromString(customerId)));
                }

                return reading;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read reading", e);
        }
        return null;
    }



    public List<IReading> readAll() {
        List<IReading> readings = new ArrayList<>();
        String sql = "SELECT * FROM readings";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Create and populate the Reading object directly here
                Reading reading = new Reading();
                reading.setId(UUID.fromString(rs.getString("id")));
                reading.setMeterId(rs.getString("meter_reading")); // Assuming meter_reading corresponds to meterId
                reading.setKindOfMeter(KindOfMeter.valueOf(rs.getString("kind_of_meter")));
                reading.setMeterCount(rs.getDouble("meter_count"));
                reading.setDateOfReading(rs.getDate("reading_date").toLocalDate());
                reading.setSubstitute(rs.getBoolean("substitute"));
                reading.setComment(rs.getString("comment"));

                String customerId = rs.getString("customer_id");
                if (customerId != null) {
                    reading.setCustomer(CustomerDAO.getInstance().read(UUID.fromString(customerId)));
                }

                readings.add(reading);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read readings", e);
        }
        return readings;
    }

    public void update(IReading reading) {
        String sql = "UPDATE readings SET meter_reading = ?, kind_of_meter = ?, meter_count = ?, " +
                "reading_date = ?, substitute = ?, comment = ?, customer_id = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, reading.getMeterCount());
            stmt.setString(2, reading.getKindOfMeter().toString());
            stmt.setDouble(3, reading.getMeterCount());
            stmt.setDate(4, java.sql.Date.valueOf(reading.getDateOfReading()));
            stmt.setBoolean(5, reading.getSubstitute());
            stmt.setString(6, reading.getComment());
            stmt.setString(7, reading.getCustomer() != null ? reading.getCustomer().getId().toString() : null);
            stmt.setString(8, reading.getId().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update reading", e);
        }
    }

    public void delete(UUID id) {
        String sql = "DELETE FROM readings WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete reading", e);
        }
    }

    // Methode zum Laden der Datenbank-Eigenschaften
    private Properties loadDatabaseProperties() {
        Properties properties = new Properties();
        properties.setProperty("user.name.db.url", "jdbc:mariadb://localhost:3306/hausfix_db");
        properties.setProperty("user.name.db.user", "root");
        properties.setProperty("user.name.db.pw", "1234");
        return properties;
    }
}