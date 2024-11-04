package de;

import de.bs.hausfix.dao.CustomerDAO;
import de.bs.hausfix.dao.ReadingDAO;
import de.bs.hausfix.db.DatabaseConnection;
import de.bs.hausfix.model.*;

import java.util.Properties;
import java.util.UUID;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Properties für die Datenbankverbindung laden
            Properties dbProperties = loadDatabaseProperties();

            // Datenbankverbindung über Singleton herstellen
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            dbConnection.openConnection(dbProperties);

            // Tabellen erstellen/aktualisieren
            dbConnection.createAllTables();

            // DAOs initialisieren
            CustomerDAO customerDAO = new CustomerDAO(dbConnection);
            ReadingDAO readingDAO = new ReadingDAO(dbConnection, customerDAO);

            // Demo der CRUD-Operationen
            demonstrateCRUDOperations(customerDAO, readingDAO);

            // Verbindung schließen
            dbConnection.closeConnection();

        } catch (Exception e) {
            System.err.println("Ein Fehler ist aufgetreten: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void demonstrateCRUDOperations(CustomerDAO customerDAO, ReadingDAO readingDAO) {
        System.out.println("=== Starte CRUD-Demonstration ===");

        // Kunde erstellen
        ICustomer customer = new Customer();
        customer.setFirstName("Max");
        customer.setLastName("Mustermann");
        customer.setBirthDate(LocalDate.of(1990, 1, 1));
        customer.setGender(Gender.M);
        customerDAO.create(customer);
        System.out.println("Kunde erstellt: " + customer.getFirstName() + " " + customer.getLastName() +
                " (ID: " + customer.getId() + ")");

        // Erste Ablesung erstellen
        IReading reading1 = new Reading();
        reading1.setCustomer(customer);
        reading1.setDateOfReading(LocalDate.now());
        reading1.setKindOfMeter(KindOfMeter.WASSER);
        reading1.setMeterCount(123.45);
        reading1.setMeterId("WASSER-001");
        reading1.setSubstitute(false);
        reading1.setComment("Erste Wasserablesung");
        readingDAO.create(reading1);

        // Zweite Ablesung für denselben Kunden
        IReading reading2 = new Reading();
        reading2.setCustomer(customer);
        reading2.setDateOfReading(LocalDate.now());
        reading2.setKindOfMeter(KindOfMeter.STROM);
        reading2.setMeterCount(5467.8);
        reading2.setMeterId("STROM-001");
        reading2.setSubstitute(false);
        reading2.setComment("Erste Stromablesung");
        readingDAO.create(reading2);

        System.out.println("\n=== Alle Kunden auslesen ===");
        List<ICustomer> allCustomers = customerDAO.readAll();
        for (ICustomer c : allCustomers) {
            System.out.println("Kunde: " + c.getFirstName() + " " + c.getLastName() +
                    " (Geboren: " + c.getBirthDate() + ")");
        }

        System.out.println("\n=== Alle Ablesungen auslesen ===");
        List<IReading> allReadings = readingDAO.readAll();
        for (IReading r : allReadings) {
            System.out.println("Ablesung: " + r.getKindOfMeter() +
                    " Zähler: " + r.getMeterId() +
                    " Stand: " + r.getMeterCount() +
                    " Kunde: " + r.getCustomer().getLastName());
        }

        // Update Demonstration
        System.out.println("\n=== Update Demonstration ===");
        customer.setFirstName("Maximilian");
        customerDAO.update(customer);
        System.out.println("Kundenname aktualisiert");

        reading1.setMeterCount(125.45);
        readingDAO.update(reading1);
        System.out.println("Zählerstand aktualisiert");

        // Delete Demonstration
        System.out.println("\n=== Delete Demonstration ===");
        readingDAO.delete(reading1.getId());
        readingDAO.delete(reading2.getId());
        customerDAO.delete(customer.getId());
        System.out.println("Ablesungen und Kunde gelöscht");

        System.out.println("\n=== CRUD-Demonstration abgeschlossen ===");
    }

    private static Properties loadDatabaseProperties() {
        Properties properties = new Properties();
        String username = System.getProperty("user.name");

        try {
            // Versuche zuerst, die Properties aus dem Home-Verzeichnis zu laden
            String homeDir = System.getProperty("user.home");
            java.nio.file.Path propPath = java.nio.file.Paths.get(homeDir, "db.properties");

            if (java.nio.file.Files.exists(propPath)) {
                properties.load(new java.io.FileInputStream(propPath.toFile()));
            } else {
                // Wenn nicht im Home-Verzeichnis, dann aus dem Classpath laden
                properties.load(Main.class.getResourceAsStream("/db.properties"));
            }

            // Überprüfe, ob alle erforderlichen Properties vorhanden sind
            String[] requiredProps = {
                    username + ".db.url",
                    username + ".db.user",
                    username + ".db.pw"
            };

            for (String prop : requiredProps) {
                if (!properties.containsKey(prop)) {
                    throw new RuntimeException("Erforderliche Property nicht gefunden: " + prop);
                }
            }

            return properties;
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Laden der Datenbank-Properties", e);
        }
    }
}