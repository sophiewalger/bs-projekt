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
        List<ICustomer> customers = customerDAO.readAll();
        return Response.ok(customers).build();
    }

    // GET: A specific customer
    @GET
    @Path("/customers/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("uuid") UUID id) {
        ICustomer customer = customerDAO.read(id);
        if (customer != null) {
            return Response.ok(customer).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // POST: Create a new customer
    @POST
    @Path("/customers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomer(Customer customer) {
        customer.setId(UUID.randomUUID());
        customerDAO.create(customer);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    // PUT: Update a customer
    @PUT
    @Path("/customers/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("uuid") UUID id, Customer updatedCustomer) {
        ICustomer existingCustomer = customerDAO.read(id);
        if (existingCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Update customer details
        existingCustomer.setFirstName(updatedCustomer.getFirstName());
        existingCustomer.setLastName(updatedCustomer.getLastName());
        existingCustomer.setBirthDate(updatedCustomer.getBirthDate());
        existingCustomer.setGender(updatedCustomer.getGender());
        existingCustomer.setStreet(updatedCustomer.getStreet());
        existingCustomer.setHouseNumber(updatedCustomer.getHouseNumber());
        existingCustomer.setPostcode(updatedCustomer.getPostcode());
        existingCustomer.setCity(updatedCustomer.getCity());

        customerDAO.update(existingCustomer);
        return Response.ok(existingCustomer).build();
    }

    // DELETE: Delete a customer
    @DELETE
    @Path("/customers/{uuid}")
    public Response deleteCustomer(@PathParam("uuid") UUID id) {
        ICustomer existingCustomer = customerDAO.read(id);
        if (existingCustomer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        customerDAO.delete(id);
        return Response.noContent().build();
    }

    // GET: All readings
    @GET
    @Path("/readings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReadings() {
        List<IReading> readings = readingDAO.readAll();
        return Response.ok(readings).build();
    }

    // GET: A specific reading
    @GET
    @Path("/readings/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReading(@PathParam("uuid") UUID id) {
        IReading reading = readingDAO.read(id);
        if (reading != null) {
            return Response.ok(reading).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // POST: Create a new reading
    @POST
    @Path("/readings")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReading(Reading reading) {
        // Validate customer ID
        if (reading.getCustomerId() == null || reading.getCustomerId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Customer ID is required").build();
        }

        // Fetch the customer to ensure it exists
        ICustomer customer = customerDAO.read(UUID.fromString(reading.getCustomerId()));
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
        }

        // Set the ID for the reading
        reading.setId(UUID.randomUUID());
        readingDAO.create(reading); // Save the reading to the database

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }

    // PUT: Update a reading
    @PUT
    @Path("/readings/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReading(@PathParam("uuid") UUID id, Reading updatedReading) {
        IReading existingReading = readingDAO.read(id);
        if (existingReading == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
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
    }

    // DELETE: Delete a reading
    @DELETE
    @Path("/readings/{uuid}")
    public Response deleteReading(@PathParam("uuid") UUID id) {
        IReading existingReading = readingDAO.read(id);
        if (existingReading == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        readingDAO.delete(id);
        return Response.noContent().build();
    }
}