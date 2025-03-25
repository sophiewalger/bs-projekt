package de.bs.hausfix;

import de.bs.hausfix.dao.CustomerDAO;
import de.bs.hausfix.dao.ReadingDAO;
import de.bs.hausfix.db.DatabaseConnection;
import de.bs.hausfix.model.*;
import de.bs.hausfix.server.Server;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class App {
    private static CustomerDAO customerDAO;
    private static ReadingDAO readingDAO;

    public static void main(String[] args) {
        try {
            Properties dbProperties = loadDatabaseProperties();

            // Datenbankverbindung über Singleton herstellen
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            dbConnection.openConnection(dbProperties);

            // Tabellen erstellen/aktualisieren
            dbConnection.createAllTables();

            Server.startServer("http://localhost:8081/rest");

        } catch (Exception e) {
            System.err.println("Ein Fehler ist aufgetreten: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Properties loadDatabaseProperties() {
        Properties properties = new Properties();

        try {
            // Lade direkt aus dem resources Verzeichnis
            InputStream input = App.class.getClassLoader().getResourceAsStream("database.properties");

            if (input == null) {
                throw new RuntimeException("database.properties nicht gefunden im Classpath");
            }

            properties.load(input);

            // Überprüfe, ob alle erforderlichen Properties vorhanden sind
            String[] requiredProps = {
                    "user.name" + ".db.url",
                    "user.name" + ".db.user",
                    "user.name" + ".db.pw"
            };

            for (String prop : requiredProps) {
                if (!properties.containsKey(prop)) {
                    throw new RuntimeException("Erforderliche Property nicht gefunden: " + prop);
                }
            }

            return properties;

        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Laden der Datenbank-Properties", e);
        }
    }
}