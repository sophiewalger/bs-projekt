package tests;


import de.bs.hausfix.db.DatabaseConnection;
import de.bs.hausfix.model.Customer;
import de.bs.hausfix.model.Gender;
import de.bs.hausfix.model.ICustomer;
import org.junit.jupiter.api.*;
import de.bs.hausfix.dao.CustomerDAO;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDAOTest {
    private static DatabaseConnection dbConnection;
    private static CustomerDAO customerDAO;
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
    }

    @Test
    void testCreateCustomer() {
        customerDAO.create(testCustomer);
        assertNotNull(testCustomer.getId());

        ICustomer retrievedCustomer = customerDAO.read(testCustomer.getId());
        assertNotNull(retrievedCustomer);
        assertEquals(testCustomer.getFirstName(), retrievedCustomer.getFirstName());
        assertEquals(testCustomer.getLastName(), retrievedCustomer.getLastName());
        assertEquals(testCustomer.getBirthDate(), retrievedCustomer.getBirthDate());
        assertEquals(testCustomer.getGender(), retrievedCustomer.getGender());
    }

    @Test
    void testReadCustomer() {
        customerDAO.create(testCustomer);
        ICustomer retrievedCustomer = customerDAO.read(testCustomer.getId());

        assertNotNull(retrievedCustomer);
        assertEquals(testCustomer.getId(), retrievedCustomer.getId());
    }

    @Test
    void testReadNonExistentCustomer() {
        ICustomer retrievedCustomer = customerDAO.read(UUID.randomUUID());
        assertNull(retrievedCustomer);
    }

    @Test
    void testUpdateCustomer() {
        customerDAO.create(testCustomer);

        testCustomer.setFirstName("John");
        testCustomer.setLastName("Doe");
        customerDAO.update(testCustomer);

        ICustomer updatedCustomer = customerDAO.read(testCustomer.getId());
        assertEquals("John", updatedCustomer.getFirstName());
        assertEquals("Doe", updatedCustomer.getLastName());
    }

    @Test
    void testDeleteCustomer() {
        customerDAO.create(testCustomer);
        assertNotNull(customerDAO.read(testCustomer.getId()));

        customerDAO.delete(testCustomer.getId());
        assertNull(customerDAO.read(testCustomer.getId()));
    }

    @Test
    void testReadAllCustomers() {
        // Create first test customer
        customerDAO.create(testCustomer);

        // Create second test customer
        ICustomer secondCustomer = new Customer();
        secondCustomer.setFirstName("Jane");
        secondCustomer.setLastName("Doe");
        secondCustomer.setBirthDate(LocalDate.of(1995, 5, 5));
        secondCustomer.setGender(Gender.F);
        customerDAO.create(secondCustomer);

        List<ICustomer> customers = customerDAO.readAll();
        assertEquals(2, customers.size());
    }

    @AfterAll
    static void tearDown() {
        dbConnection.removeAllTables();
        dbConnection.closeConnection();
    }
}