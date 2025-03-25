package de.bs.hausfix.model;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public interface ICustomer extends IID {
    void setFirstName(String firstName);
    void setLastName(String lastname);
    void setBirthDate(LocalDate birthDate);
    void setGender(Gender gender);

    // Neue Methoden für die zusätzlichen Felder
    void setStreet(String street);
    void setHouseNumber(String houseNumber);
    void setPostcode(String postcode);
    void setCity(String city);

    String getFirstName();
    String getLastName();
    LocalDate getBirthDate();
    @NotNull Gender getGender();

    // Neue Getter für die zusätzlichen Felder
    String getStreet();
    String getHouseNumber();
    String getPostcode();
    String getCity();
}