package de.bs.hausfix.dao;

import de.bs.hausfix.db.DatabaseConnection;
import de.bs.hausfix.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReadingDAO {
    private final Connection connection;
    private final CustomerDAO customerDAO;

    public ReadingDAO(DatabaseConnection dbConnection, CustomerDAO customerDAO) {
        this.connection = dbConnection.getConnection();
        this.customerDAO = customerDAO;
    }

    public void create(IReading reading) {
        // Ensure customer exists if provided
        if (reading.getCustomer() != null) {
            ICustomer existingCustomer = customerDAO.read(reading.getCustomer().getId());
            if (existingCustomer == null) {
                customerDAO.create(reading.getCustomer());
            }
        }

        String sql = "INSERT INTO readings (id, meter_id, kind_of_meter, meter_count, date_of_reading, " +
                "substitute, comment, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (reading.getId() == null) {
                reading.setId(UUID.randomUUID());
            }

            stmt.setString(1, reading.getId().toString());
            stmt.setString(2, reading.getMeterId());
            stmt.setString(3, reading.getKindOfMeter().toString());
            stmt.setDouble(4, reading.getMeterCount());
            stmt.setDate(5, Date.valueOf(reading.getDateOfReading()));
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
                return mapResultSetToReading(rs);
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
                readings.add(mapResultSetToReading(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read readings", e);
        }
        return readings;
    }

    public void update(IReading reading) {
        String sql = "UPDATE readings SET meter_id = ?, kind_of_meter = ?, meter_count = ?, " +
                "date_of_reading = ?, substitute = ?, comment = ?, customer_id = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reading.getMeterId());
            stmt.setString(2, reading.getKindOfMeter().toString());
            stmt.setDouble(3, reading.getMeterCount());
            stmt.setDate(4, Date.valueOf(reading.getDateOfReading()));
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

    private IReading mapResultSetToReading(ResultSet rs) throws SQLException {
        IReading reading = new Reading();
        reading.setId(UUID.fromString(rs.getString("id")));
        reading.setMeterId(rs.getString("meter_id"));
        reading.setKindOfMeter(KindOfMeter.valueOf(rs.getString("kind_of_meter")));
        reading.setMeterCount(rs.getDouble("meter_count"));
        reading.setDateOfReading(rs.getDate("date_of_reading").toLocalDate());
        reading.setSubstitute(rs.getBoolean("substitute"));
        reading.setComment(rs.getString("comment"));

        String customerId = rs.getString("customer_id");
        if (customerId != null) {
            reading.setCustomer(customerDAO.read(UUID.fromString(customerId)));
        }

        return reading;
    }
}