package de.bs.hausfix.server;
import de.bs.hausfix.dao.CustomerDAO;
import de.bs.hausfix.model.*;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/resources")
public class RestApi {

    // Beispielhafte Datenbank (in der Realität würdest du eine echte Datenbank verwenden)
    private static List<Customer> customers = new ArrayList<>();

    // GET: Alle Kunden abrufen
//    @GET
//    @Path("/customers")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Customer> getAllCustomers() {
//        return customers;
//    }
    @GET
    @Path("/customers") // Pfad für alle Kunden
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> getAllCustomers() {
        return CustomerDAO;}

    // GET: Einen bestimmten Kunden abrufen
    @GET
    @Path("/customers/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomer(@PathParam("uuid") UUID id) {
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                System.out.println(customer.getFirstName());
                return Response.ok(customer).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // POST: Neuen Kunden erstellen
    @POST
    @Path("/customers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomer(Customer customer) {
        customer.setId(UUID.randomUUID());
        customers.add(customer);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    // PUT: Kunden aktualisieren
    @PUT
    @Path("/customers/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("uuid") UUID id, Customer updatedCustomer) {
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                customer.setFirstName(updatedCustomer.getFirstName());
                customer.setLastName(updatedCustomer.getLastName());
                customer.setBirthDate(updatedCustomer.getBirthDate());
                customer.setGender(updatedCustomer.getGender());
                return Response.ok(customer).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // DELETE: Kunden löschen
    @DELETE
    @Path("/customers/{uuid}")
    public Response deleteCustomer(@PathParam("uuid") UUID id) {
        for (Customer customer : customers) {
            if (customer.getId().equals(id)) {
                customers.remove(customer);
                return Response.noContent().build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Beispielhafte Datenbank (in der Realität würdest du eine echte Datenbank verwenden)
    private static List<Reading> readings = new ArrayList<>();

    // GET: Alle Kunden abrufen
    @GET
    @Path("/readings")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reading> getAllReadings() {
        return readings;
    }

    // GET: Einen bestimmten Kunden abrufen
    @GET
    @Path("/readings/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReading(@PathParam("uuid") UUID id) {
        for (Reading reading : readings) {
            if (reading.getId().equals(id)) {
                return Response.ok(reading).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // POST: Neuen Kunden erstellen
    @POST
    @Path("/readings")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReading(Reading reading) {
        reading.setId(UUID.randomUUID());
        readings.add(reading);
        return Response.status(Response.Status.CREATED).entity(reading).build();
    }

    // PUT: Kunden aktualisieren
    @PUT
    @Path("/readings/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReading(@PathParam("uuid") UUID id, Reading updatedReading) {
        for (Reading reading : readings) {
            if (reading.getId().equals(id)) {
                reading.setMeterId(updatedReading.getMeterId());
                reading.setKindOfMeter(updatedReading.getKindOfMeter());
                reading.setMeterCount(updatedReading.getMeterCount());
                reading.setDateOfReading(updatedReading.getDateOfReading());
                reading.setSubstitute(updatedReading.getSubstitute());
                reading.setComment(updatedReading.getComment());
                reading.setCustomer(updatedReading.getCustomer());
                return Response.ok(reading).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // DELETE: Kunden löschen
    @DELETE
    @Path("/readings/{uuid}")
    public Response deleteReading(@PathParam("uuid") UUID id) {
        for (Reading reading : readings) {
            if (reading.getId().equals(id)) {
                readings.remove(reading);
                return Response.noContent().build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}