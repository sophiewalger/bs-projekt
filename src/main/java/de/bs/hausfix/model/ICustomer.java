package de.bs.hausfix.model;

import java.time.LocalDate;

public interface ICustomer extends IID {
    void setFirstName(String firstName);
    void setLastName(String lastname);
    void setBirthDate(LocalDate birthDate);
    void setGender(Gender gender);

    String getFirstName();
    String getLastName();
    LocalDate getBirthDate();
    Gender getGender();
}