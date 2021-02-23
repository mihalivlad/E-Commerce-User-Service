package com.example.UserService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddressDTO {
    private String country;
    private String county;
    private String city;
    private String street;
    private int nr;
    private String postalCode;
}
