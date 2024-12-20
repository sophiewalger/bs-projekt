package de.bs.hausfix.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.constraints.NotNull;


import java.time.format.DateTimeFormatter;
import java.util.UUID;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "reading")
public class Reading implements IReading {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("meterId")
    @NotNull
    private String meterId;
    @JsonProperty("kindOfMeter")
    @NotNull
    private KindOfMeter kindOfMeter;
    @JsonProperty("meterCount")
    @NotNull
    private Double meterCount;
    @JsonProperty("dateOfReading")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate dateOfReading;
    @JsonProperty("substitute")
    private Boolean substitute;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("customer")
    private ICustomer customer;


    @Override
    public UUID getId() {
        return id;
    }



    @Override
    public void setId(UUID id) {
        this.id = id;
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
    public void setId(String id) {

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

    @Override
    public ICustomer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(ICustomer customer) {
        this.customer = customer;
    }

    @Override
    public String printDateOfReading() {
        if (dateOfReading == null) {
            return "";
        }
        return dateOfReading.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}