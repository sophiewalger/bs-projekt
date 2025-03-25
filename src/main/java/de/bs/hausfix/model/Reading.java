package de.bs.hausfix.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import de.bs.hausfix.dao.CustomerDAO;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "reading")
public class Reading implements IReading {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("meter_reading")
    @NotNull
    private String meterId;

    @JsonProperty("kind_of_meter")
    @NotNull
    private KindOfMeter kindOfMeter;

    @JsonProperty("meter_count")
    @NotNull
    private Double meterCount;

    @JsonProperty("reading_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfReading;

    @JsonProperty("substitute")
    private Boolean substitute;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("customer_id")
    private String customerId; // Store customer ID as a String

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public void setId(String id) {
        this.id = UUID.fromString(id); // Convert String to UUID
    }

    @Override
    public String getMeterId() {
        return meterId;
    }

    @Override
    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    @Override
    public KindOfMeter getKindOfMeter() {
        return kindOfMeter;
    }

    @Override
    public void setKindOfMeter(KindOfMeter kindOfMeter) {
        this.kindOfMeter = kindOfMeter;
    }

    @Override
    public Double getMeterCount() {
        return meterCount;
    }

    @Override
    public void setMeterCount(Double meterCount) {
        this.meterCount = meterCount;
    }

    @Override
    public LocalDate getDateOfReading() {
        return dateOfReading;
    }

    @Override
    public void setDateOfReading(LocalDate dateOfReading) {
        this.dateOfReading = dateOfReading;
    }

    @Override
    public Boolean getSubstitute() {
        return substitute;
    }

    @Override
    public void setSubstitute(Boolean substitute) {
        this.substitute = substitute;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCustomerId() {
        return customerId; // Getter for customerId
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId; // Setter for customerId
    }

    @Override
    public ICustomer getCustomer() {
        // Assuming you have a method to fetch the customer based on customerId
        return CustomerDAO.getInstance().read(UUID.fromString(customerId)); // Fetch customer using DAO
    }

    @Override
    public void setCustomer(ICustomer customer) {
        this.customerId = customer.getId().toString(); // Set customerId from the customer object
    }

    @Override
    public String printDateOfReading() {
        if (dateOfReading == null) {
            return "";
        }
        return dateOfReading.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}