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
        String sql = "INSERT INTO customers (id, firstname, lastname, birthdate , gender, street, housenumber, postcode, city) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (customer.getId() == null) {
                customer.setId(UUID.randomUUID());
            }

            stmt.setString(1, customer.getId().toString());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());
            stmt.setDate(  4, java.sql.Date.valueOf(customer.getBirthDate()));
            stmt.setString(5, customer.getGender().name());
            stmt.setString(6, customer.getStreet());
            stmt.setString(7, customer.getHouseNumber());
            stmt.setString(8, customer.getPostcode());
            stmt.setString(9, customer.getCity());

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
                // Erstellen und Befüllen des Customer-Objekts direkt hier
                Customer customer = new Customer();
                customer.setId(UUID.fromString(rs.getString("id")));
                customer.setFirstName(rs.getString("firstname"));
                customer.setLastName(rs.getString("lastname"));

                // Birthdate konvertieren
                if (rs.getDate("birthdate") != null) {
                    customer.setBirthDate(rs.getDate("birthdate").toLocalDate());
                } else {
                    customer.setBirthDate(null);
                }

                // Gender konvertieren
                String genderString = rs.getString("gender");
                if (genderString != null) {
                    customer.setGender(Gender.valueOf(genderString)); // Stellen Sie sicher, dass die Enum-Werte übereinstimmen
                } else {
                    customer.setGender(null);
                }

                customer.setStreet(rs.getString("street"));
                customer.setHouseNumber(rs.getString("housenumber"));
                customer.setPostcode(rs.getString("postcode"));
                customer.setCity(rs.getString("city"));

                return customer;
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
                // Erstellen und Befüllen des Customer-Objekts direkt hier
                Customer customer = new Customer();
                customer.setId(UUID.fromString(rs.getString("id")));
                customer.setFirstName(rs.getString("firstname"));
                customer.setLastName(rs.getString("lastname"));

                // Birthdate konvertieren
                if (rs.getDate("birthdate") != null) {
                    customer.setBirthDate(rs.getDate("birthdate").toLocalDate());
                } else {
                    customer.setBirthDate(null);
                }

                // Gender konvertieren
                String genderString = rs.getString("gender");
                if (genderString != null) {
                    customer.setGender(Gender.valueOf(genderString)); // Stellen Sie sicher, dass die Enum-Werte übereinstimmen
                } else {
                    customer.setGender(null);
                }

                customer.setStreet(rs.getString("street"));
                customer.setHouseNumber(rs.getString("housenumber"));
                customer.setPostcode(rs.getString("postcode"));
                customer.setCity(rs.getString("city"));

                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read customers", e);
        }
        return customers;
    }

    public void update(ICustomer customer) {
        String sql = "UPDATE customers SET firstname = ?, lastname = ?, birthdate = ?, gender = ?, street = ?, housenumber = ?, postcode = ?, city = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            System.out.println("Executing SQL update for customer: " + customer.getId());
            System.out.println("New values: " + customer.getFirstName() + " " + customer.getLastName());
            
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setDate(3, java.sql.Date.valueOf(customer.getBirthDate()));
            stmt.setString(4, customer.getGender().name());
            stmt.setString(5, customer.getStreet());
            stmt.setString(6, customer.getHouseNumber());
            stmt.setString(7, customer.getPostcode());
            stmt.setString(8, customer.getCity());
            stmt.setString(9, customer.getId().toString());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected by update: " + rowsAffected);
            
        } catch (SQLException e) {
            System.err.println("SQL Error during update: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update customer: " + e.getMessage(), e);
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