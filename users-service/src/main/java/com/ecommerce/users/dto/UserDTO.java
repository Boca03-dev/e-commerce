package com.ecommerce.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class UserDTO {

    private Long id;

    @NotBlank(message = "Ime je obavezno")
    @Size(min = 2, max = 50, message = "Ime mora da sadrzi bar 2 i maksimum 50 slova")
    private String firstName;

    @NotBlank(message = "Prezime je obavezno")
    @Size(min = 2, max = 50, message = "Prezime mora da sadrzi bar 2 i maksimum 50 slova")
    private String lastName;

    @NotBlank(message = "Email je obavezan")
    @Email(message = "Email mora biti validan")
    private String email;

    @NotBlank(message = "Adresa je obavezna")
    @Size(max = 200, message = "Adresa ne sme imati vise od 200 karaktera")
    private String address;

    private String phoneNumber;

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String email, String address, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(email, userDTO.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
