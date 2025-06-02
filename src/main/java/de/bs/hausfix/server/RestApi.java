package de.bs.hausfix.server;

import de.bs.hausfix.dao.CustomerDAO;
import de.bs.hausfix.dao.ReadingDAO;
import de.bs.hausfix.model.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/resources")
public class RestApi {

    private final CustomerDAO customerDAO = CustomerDAO.getInstance();
    private final ReadingDAO readingDAO = ReadingDAO.getInstance();

    // GET: All customers
    @GET
    @Path("/customers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers() {
        try {
            List<ICustomer> customers = customerDAO.readAll();
            return Response.ok(customers).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Fehler beim Abrufen der Kunden: " + e.getMessage())
                .build();
        }
    }

    @OPTIONS
    @Path("/{path:.*}")
    public Response handleOptions() {
        return Response.ok().build();
    }

    // GET: A specific customer
    @GET
    @Path("/customers/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("uuid") UUID id) {
        try {
            ICustomer customer = customerDAO.read(id);
            if (customer != null) {
                return Response.ok(customer).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Kunde nicht gefunden")
                    .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Fehler beim Abrufen des Kunden: " + e.getMessage())
                .build();
        }
    }

    // POST: Create a new customer
    @POST
    @Path("/customers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomer(Customer customer) {
        try {
            if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty() ||
                customer.getLastName() == null || customer.getLastName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Vorname und Nachname sind erforderlich")
                    .build();
            }
            customer.setId(UUID.randomUUID());
            customerDAO.create(customer);
            return Response.status(Response.Status.CREATED)
                .entity(customer)
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Fehler beim Erstellen des Kunden: " + e.getMessage())
                .build();
        }
    }

    // PUT: Update a customer
    @PUT
    @Path("/customers/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("uuid") UUID id, Customer updatedCustomer) {
        try {
            System.out.println("Update Request received for ID: " + id);
            System.out.println("Updated Customer Data: " + updatedCustomer.getFirstName() + " " + updatedCustomer.getLastName());
            
            ICustomer existingCustomer = customerDAO.read(id);
            if (existingCustomer == null) {
                System.out.println("Customer not found with ID: " + id);
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Kunde nicht gefunden")
                    .build();
            }

            System.out.println("Existing Customer found: " + existingCustomer.getFirstName() + " " + existingCustomer.getLastName());
            
            // Setze die ID des existierenden Kunden
            updatedCustomer.setId(id);

            // Update customer details
            customerDAO.update(updatedCustomer);
            System.out.println("Customer updated successfully");
            
            return Response.ok(updatedCustomer).build();
        } catch (Exception e) {
            System.err.println("Error updating customer: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Fehler beim Aktualisieren des Kunden: " + e.getMessage())
                .build();
        }
    }

    // DELETE: Delete a customer
    @DELETE
    @Path("/customers/{uuid}")
    public Response deleteCustomer(@PathParam("uuid") UUID id) {
        try {
            ICustomer existingCustomer = customerDAO.read(id);
            if (existingCustomer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Kunde nicht gefunden")
                    .build();
            }

            customerDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Fehler beim Löschen des Kunden: " + e.getMessage())
                .build();
        }
    }

    // GET: All readings
    @GET
    @Path("/readings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReadings() {
        try {
            List<IReading> readings = readingDAO.readAll();
            return Response.ok(readings).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Fehler beim Abrufen der Ablesungen: " + e.getMessage())
                .build();
        }
    }

    // GET: A specific reading
    @GET
    @Path("/readings/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReading(@PathParam("uuid") UUID id) {
        try {
            IReading reading = readingDAO.read(id);
            if (reading != null) {
                return Response.ok(reading).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ablesung nicht gefunden")
                    .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Fehler beim Abrufen der Ablesung: " + e.getMessage())
                .build();
        }
    }

    // POST: Create a new reading
    @POST
    @Path("/readings")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReading(Reading reading) {
        try {
            if (reading.getCustomerId() == null || reading.getCustomerId().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Kunden-ID ist erforderlich")
                    .build();
            }

            ICustomer customer = customerDAO.read(UUID.fromString(reading.getCustomerId()));
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Kunde nicht gefunden")
                    .build();
            }

            reading.setId(UUID.randomUUID());
            readingDAO.create(reading);
            return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Fehler beim Erstellen der Ablesung: " + e.getMessage())
                .build();
        }
    }

    // PUT: Update a reading
    @PUT
    @Path("/readings/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReading(@PathParam("uuid") UUID id, Reading updatedReading) {
        try {
            IReading existingReading = readingDAO.read(id);
            if (existingReading == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ablesung nicht gefunden")
                    .build();
            }

            // Update reading details
            existingReading.setMeterId(updatedReading.getMeterId());
            existingReading.setKindOfMeter(updatedReading.getKindOfMeter());
            existingReading.setMeterCount(updatedReading.getMeterCount());
            existingReading.setDateOfReading(updatedReading.getDateOfReading());
            existingReading.setSubstitute(updatedReading.getSubstitute());
            existingReading.setComment(updatedReading.getComment());
            existingReading.setCustomer(updatedReading.getCustomer());

            readingDAO.update(existingReading);
            return Response.ok(existingReading).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Fehler beim Aktualisieren der Ablesung: " + e.getMessage())
                .build();
        }
    }

    // DELETE: Delete a reading
    @DELETE
    @Path("/readings/{uuid}")
    public Response deleteReading(@PathParam("uuid") UUID id) {
        try {
            IReading existingReading = readingDAO.read(id);
            if (existingReading == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ablesung nicht gefunden")
                    .build();
            }

            readingDAO.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Fehler beim Löschen der Ablesung: " + e.getMessage())
                .build();
        }
    }
}