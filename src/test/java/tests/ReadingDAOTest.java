package tests;

import de.bs.hausfix.dao.CustomerDAO;
import de.bs.hausfix.dao.ReadingDAO;
import de.bs.hausfix.db.DatabaseConnection;
import de.bs.hausfix.model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReadingDAOTest {
    private static DatabaseConnection dbConnection;
    private static ReadingDAO readingDAO;
    private static CustomerDAO customerDAO;
    private static IReading testReading;
    private static ICustomer testCustomer;

    @BeforeAll
    static void setUp() {
        // Initialize database connection
        Properties props = new Properties();
        props.setProperty(System.getProperty("DE085171") + ".db.url", "jdbc:mariadb://localhost:3306/hausfix");
        props.setProperty(System.getProperty("DE085171") + ".db.user", "root");
        props.setProperty(System.getProperty("DE085171") + ".db.pw", "1234");

        dbConnection = DatabaseConnection.getInstance();
        dbConnection.openConnection(props);
        dbConnection.removeAllTables();
        dbConnection.createAllTables();

        customerDAO = new CustomerDAO(dbConnection);
        readingDAO = new ReadingDAO(dbConnection, customerDAO);
    }

    @BeforeEach
    void setUpEach() {
        dbConnection.truncateAllTables();

        // Create test customer
        testCustomer = new Customer();
        testCustomer.setFirstName("Max");
        testCustomer.setLastName("Mustermann");
        testCustomer.setBirthDate(LocalDate.of(1990, 1, 1));
        testCustomer.setGender(Gender.M);
        customerDAO.create(testCustomer);

        // Create test reading
        testReading = new Reading();
        testReading.setMeterId("METER123");
        testReading.setKindOfMeter(KindOfMeter.WASSER);
        testReading.setMeterCount(1234.56);
        testReading.setDateOfReading(LocalDate.now());
        testReading.setSubstitute(false);
        testReading.setComment("Test reading");
        testReading.setCustomer(testCustomer);
    }

    @Test
    void testCreateReading() {
        readingDAO.create(testReading);
        assertNotNull(testReading.getId());

        IReading retrievedReading = readingDAO.read(testReading.getId());
        assertNotNull(retrievedReading);
        assertEquals(testReading.getMeterId(), retrievedReading.getMeterId());
        assertEquals(testReading.getKindOfMeter(), retrievedReading.getKindOfMeter());
        assertEquals(testReading.getMeterCount(), retrievedReading.getMeterCount());
        assertEquals(testReading.getDateOfReading(), retrievedReading.getDateOfReading());
        assertEquals(testReading.getSubstitute(), retrievedReading.getSubstitute());
        assertEquals(testReading.getComment(), retrievedReading.getComment());
        assertEquals(testReading.getCustomer().getId(), retrievedReading.getCustomer().getId());
    }

    @Test
    void testReadReading() {
        readingDAO.create(testReading);
        IReading retrievedReading = readingDAO.read(testReading.getId());

        assertNotNull(retrievedReading);
        assertEquals(testReading.getId(), retrievedReading.getId());
    }

    @Test
    void testReadNonExistentReading() {
        IReading retrievedReading = readingDAO.read(UUID.randomUUID());
        assertNull(retrievedReading);
    }

    @Test
    void testUpdateReading() {
        readingDAO.create(testReading);

        testReading.setMeterCount(9876.54);
        testReading.setComment("Updated comment");
        readingDAO.update(testReading);

        IReading updatedReading = readingDAO.read(testReading.getId());
        assertEquals(9876.54, updatedReading.getMeterCount());
        assertEquals("Updated comment", updatedReading.getComment());
    }

    @Test
    void testDeleteReading() {
        readingDAO.create(testReading);
        assertNotNull(readingDAO.read(testReading.getId()));

        readingDAO.delete(testReading.getId());
        assertNull(readingDAO.read(testReading.getId()));
    }

    @Test
    void testReadAllReadings() {
        // Create first test reading
        readingDAO.create(testReading);

        // Create second test reading
        IReading secondReading = new Reading();
        secondReading.setMeterId("METER456");
        secondReading.setKindOfMeter(KindOfMeter.WASSER);
        secondReading.setMeterCount(789.12);
        secondReading.setDateOfReading(LocalDate.now());
        secondReading.setSubstitute(true);
        secondReading.setComment("Second test reading");
        secondReading.setCustomer(testCustomer);
        readingDAO.create(secondReading);

        List<IReading> readings = readingDAO.readAll();
        assertEquals(2, readings.size());
    }

    @AfterAll
    static void tearDown() {
        dbConnection.removeAllTables();
        dbConnection.closeConnection();
    }
}