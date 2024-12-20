package tests;

import de.bs.hausfix.db.IDatabaseConnection;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.util.Properties;
import de.bs.hausfix.db.DatabaseConnection;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {
    private static Properties testProperties;
    private DatabaseConnection dbConnection;

    @BeforeAll
    static void setUpClass() {
        testProperties = new Properties();
        String username = System.getProperty("user.name"); // Verwenden Sie "user.name"
        testProperties.setProperty("user.name", username); // Setzen Sie den Benutzernamen
        testProperties.setProperty(username + ".db.url", "jdbc:mariadb://localhost:3306/hausfix");
        testProperties.setProperty(username + ".db.user", "root");
        testProperties.setProperty(username + ".db.pw", "1234");
        System.out.println(testProperties);
    }

    @BeforeEach
    void setUp() {
        dbConnection = DatabaseConnection.getInstance();
    }

    @Test
    void testGetInstance() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2, "Singleton pattern should return same instance");
    }

    @Test
    void testOpenConnection() {
        IDatabaseConnection connection = dbConnection.openConnection(testProperties);
        assertNotNull(connection);

        Connection sqlConnection = dbConnection.getConnection();
        assertNotNull(sqlConnection);
    }

    @Test
    void testOpenConnectionWithInvalidProperties() {
        Properties invalidProperties = new Properties();
        assertThrows(IllegalStateException.class, () -> {
            dbConnection.openConnection(invalidProperties);
        });
    }

    @Test
    void testCreateAllTables() {
        dbConnection.openConnection(testProperties);
        assertDoesNotThrow(() -> dbConnection.createAllTables());
    }

    @Test
    void testTruncateAllTables() {
        dbConnection.openConnection(testProperties);
        dbConnection.createAllTables();
        assertDoesNotThrow(() -> dbConnection.truncateAllTables());
    }

    @Test
    void testRemoveAllTables() {
        dbConnection.openConnection(testProperties);
        dbConnection.createAllTables();
        assertDoesNotThrow(() -> dbConnection.removeAllTables());
    }

    @Test
    void testCloseConnection() {
        dbConnection.openConnection(testProperties);
        assertDoesNotThrow(() -> dbConnection.closeConnection());
    }

    @Test
    void testDatabaseOperationsSequence() {
        dbConnection.openConnection(testProperties);
        dbConnection.createAllTables();
        dbConnection.truncateAllTables();
        dbConnection.removeAllTables();
        dbConnection.closeConnection();
    }

    @AfterEach
    void tearDown() {
        if (dbConnection != null) {
            dbConnection.closeConnection();
        }
    }
}