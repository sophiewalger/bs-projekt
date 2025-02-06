package de.bs.hausfix.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "customer")
public class Customer implements ICustomer {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("firstName")
    @NotNull
    private String firstName;

    @JsonProperty("lastName")
    @NotNull
    private String lastName;

    @JsonProperty("birthDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    @JsonProperty("gender")
    @NotNull
    private Gender gender;

    // Neue Felder
    @JsonProperty("street")
    private String street;

    @JsonProperty("houseNumber")
    private String houseNumber;

    @JsonProperty("postcode")
    private String postcode;

    @JsonProperty("city")
    private String city;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    // Neue Getter und Setter für die zusätzlichen Felder
    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String getHouseNumber() {
        return houseNumber;
    }

    @Override
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String getPostcode() {
        return postcode;
    }

    @Override
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }
}