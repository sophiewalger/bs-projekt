package de.bs.hausfix.dao;

import de.bs.hausfix.db.DatabaseConnection;
import de.bs.hausfix.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class CustomerDAO {
    private static CustomerDAO instance; // Singleton-Instanz
    private final Connection connection;

    // Privater Konstruktor
    public CustomerDAO() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Properties properties = loadDatabaseProperties(); // Methode zum Laden der DB-Eigenschaften
        dbConnection.openConnection(properties); // Verbindung öffnen
        this.connection = dbConnection.getConnection(); // Verbindung abrufen
    }

    // Statische Methode zur Rückgabe der Singleton-Instanz
    public static CustomerDAO getInstance() {
        if (instance == null) {
            instance = new CustomerDAO();
        }
        return instance;
    }

    public void create(ICustomer customer) {
        String sql = "INSERT INTO customers (id, firstname, lastname, street, housenumber, postcode, city) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (customer.getId() == null) {
                customer.setId(UUID.randomUUID());
            }

            stmt.setString(1, customer.getId().toString());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());
            stmt.setString(4, customer.getStreet());
            stmt.setString(5, customer.getHouseNumber());
            stmt.setString(6, customer.getPostcode());
            stmt.setString(7, customer.getCity());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create customer", e);
        }
    }

    public ICustomer read(UUID id) {
        String sql = "SELECT * FROM customers WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.toString());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read customer", e);
        }
        return null;
    }

    public List<ICustomer> readAll() {
        List<ICustomer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read customers", e);
        }
        return customers;
    }

    public void update(ICustomer customer) {
        String sql = "UPDATE customers SET firstname = ?, lastname = ?, street = ?, housenumber = ?, postcode = ?, city = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getStreet());
            stmt.setString(4, customer.getHouseNumber());
            stmt.setString(5, customer.getPostcode());
            stmt.setString(6, customer.getCity());
            stmt.setString(7, customer.getId().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update customer", e);
        }
    }

    public void delete(UUID id) {
        String sql = "DELETE FROM customers WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete customer", e);
        }
    }

    private ICustomer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        ICustomer customer = new Customer();
        String uuidString = rs.getString("id");
        try {
            customer.setId(UUID.fromString(uuidString));
        } catch (IllegalArgumentException e) {
            System.err.println("Ungültige UUID: " + uuidString);
            // Hier können Sie entscheiden, wie Sie mit ungültigen UUIDs umgehen möchten
        }
        customer.setFirstName(rs.getString("firstname"));
        customer.setLastName(rs.getString("lastname"));
        customer.setStreet(rs.getString("street"));
        customer.setHouseNumber(rs.getString("housenumber"));
        customer.setPostcode(rs.getString("postcode"));
        customer.setCity(rs.getString("city"));
        return customer;
    }

    // Methode zum Laden der Datenbank-Eigenschaften
    private Properties loadDatabaseProperties() {
        Properties properties = new Properties();
        // Hier können Sie die Logik zum Laden der Eigenschaften implementieren
        // Zum Beispiel aus einer Datei oder Umgebungsvariablen
        properties.setProperty("user.name.db.url", "jdbc:mariadb://localhost:3306/hausfix_db");
        properties.setProperty("user.name.db.user", "root");
        properties.setProperty("user.name.db.pw", "1234");
        return properties;
    }
}