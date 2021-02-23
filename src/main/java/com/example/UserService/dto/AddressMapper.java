package com.example.UserService.dto;

import com.example.UserService.model.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address addressDTOtoAddress(AddressDTO addressDTO);
    AddressDTO addressToAddressDTO(Address address);
}
