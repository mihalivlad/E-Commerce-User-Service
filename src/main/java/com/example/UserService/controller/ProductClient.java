package com.example.UserService.controller;

import com.example.UserService.dto.CategoryDTO;
import com.example.UserService.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@ComponentScan("com.example.UserService.config")
public class ProductClient {

    Logger logger = LoggerFactory.getLogger(ProductClient.class);

    RestTemplate restTemplate;
    HttpHeaders httpHeaders;

    @Value("${app.url.products}")
    String url;

    @Autowired
    public ProductClient(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    public Long getCategories() {
        ResponseEntity<CategoryDTO[]> responseEntity = restTemplate.getForEntity(url + "/category/retrieveCategories", CategoryDTO[].class);
        logger.info("Requested the categories from http://localhost:8081");
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            Random random = new Random();
            List<CategoryDTO> categoryDTOList = Arrays.asList(responseEntity.getBody());
            Long categoryId = categoryDTOList.get(random.nextInt(categoryDTOList.size() - 1)).getId();
            return categoryId;
        }
        logger.warn("Request returned " + responseEntity.getStatusCode());
        return null;
    }

    public List<ProductDTO> getProducts(Long categoryId) {
        ResponseEntity<ProductDTO[]> responseEntity = restTemplate.getForEntity(url + "/retrieveProductsByCategory/" + categoryId, ProductDTO[].class);
        logger.info("Requested the products by category from http://localhost:8081");
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return Arrays.asList(responseEntity.getBody());
        }
        logger.warn("Request returned " + responseEntity.getStatusCode());
        return null;
    }

}
