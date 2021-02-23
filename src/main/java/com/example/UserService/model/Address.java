package com.example.UserService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
@Entity
public class Address{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Country field must not be blank")
    private String country;
    @NotBlank(message = "County field must not be blank")
    private String county;
    @NotBlank(message = "City field must not be blank")
    private String city;
    @NotBlank(message = "Street field must not be blank")
    private String street;
    @Min(value = 1,message = "Number field must not be less than 1" )
    private int nr;
    @Size(min = 6, max = 6, message = "Postal field must be 6 digits")
    private String postalCode;

    @JsonIgnore
    @OneToOne(mappedBy = "billingAddress")
    private User userForBilling;
    @JsonIgnore
    @OneToOne(mappedBy = "deliveryAddress")
    private User userForDelivery;

    public Address(String country, String county, String city, String street, int nr, String postalCode) {
        this.country = country;
        this.county = county;
        this.city = city;
        this.street = street;
        this.nr = nr;
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Address))
            return false;
        Address address = (Address) obj;
        return country.equals(address.country) && county.equals(address.county) && city.equals(address.city) && street.equals(address.street) && nr == address.nr && postalCode.equals(address.postalCode);
    }
}
