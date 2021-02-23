package com.example.UserService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String upc;
}
