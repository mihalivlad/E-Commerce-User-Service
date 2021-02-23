package com.example.UserService.dto;

import com.example.UserService.model.Address;
import com.example.UserService.model.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User userDTOtoUser(UserDTO userDTO);
    UserDTO userToUserDTO(User user);

    Address addressDTOtoAddress(AddressDTO addressDTO);
    AddressDTO addressToAddressDTO(Address address);
}
