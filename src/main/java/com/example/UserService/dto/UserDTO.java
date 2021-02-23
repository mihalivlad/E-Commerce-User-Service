package com.example.UserService.dto;

import com.example.UserService.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDTO {
    private String username;
    private String password;
    private Address deliveryAddress;
    private Address billingAddress;

}
