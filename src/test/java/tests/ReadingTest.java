package tests;

import de.bs.hausfix.model.Customer;
import de.bs.hausfix.model.KindOfMeter;
import de.bs.hausfix.model.Reading;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReadingTest {
    private Reading reading;
    private Customer customer;

    @BeforeEach
    void setUp() {
        reading = new Reading();
        customer = new Customer();
        customer.setId(UUID.randomUUID());
    }

    @Test
    void testReadingProperties() {
        UUID id = UUID.randomUUID();
        String meterId = "METER123";
        KindOfMeter kindOfMeter = KindOfMeter.WASSER;
        double meterCount = 1234.56;
        LocalDate dateOfReading = LocalDate.now();
        boolean substitute = false;
        String comment = "Test reading";

        reading.setId(id);
        reading.setMeterId(meterId);
        reading.setKindOfMeter(kindOfMeter);
        reading.setMeterCount(meterCount);
        reading.setDateOfReading(dateOfReading);
        reading.setSubstitute(substitute);
        reading.setComment(comment);
        reading.setCustomer(customer);

        assertEquals(id, reading.getId());
        assertEquals(meterId, reading.getMeterId());
        assertEquals(kindOfMeter, reading.getKindOfMeter());
        assertEquals(meterCount, reading.getMeterCount());
        assertEquals(dateOfReading, reading.getDateOfReading());
        assertEquals(substitute, reading.getSubstitute());
        assertEquals(comment, reading.getComment());
        assertEquals(customer, reading.getCustomer());
    }

    @Test
    void testReadingEquality() {
        Reading reading1 = new Reading();
        Reading reading2 = new Reading();

        UUID id = UUID.randomUUID();
        reading1.setId(id);
        reading2.setId(id);

        assertEquals(reading1, reading2);
        assertEquals(reading1.hashCode(), reading2.hashCode());
    }

    @Test
    void testReadingWithNullCustomer() {
        reading.setCustomer(null);
        assertNull(reading.getCustomer());
    }
}