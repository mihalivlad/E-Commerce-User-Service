package com.example.UserService.controller;

import com.example.UserService.config.AppConfig;
import com.example.UserService.dto.OrderDTO;
import com.example.UserService.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@ComponentScan("com.example.UserService.config")
public class ShoppingCartClient {

    Logger logger = LoggerFactory.getLogger(ShoppingCartClient.class);

    RestTemplate restTemplate;
    HttpHeaders httpHeaders;

    @Value("${app.url.shopping-cart}")
    String url;

    @Autowired
    public ShoppingCartClient(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    public String buyProducts(String username, List<ProductDTO> productDTOS) {

//        List<Long> productsId = getRandomProducts(productDTOS);
        List<Long> productsId = productDTOS.stream().map(ProductDTO::getId).collect(Collectors.toList());
        OrderDTO orderDTO = new OrderDTO(username, productsId);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<OrderDTO> request = new HttpEntity<>(orderDTO, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url + "/saveOrder", request, String.class);
        logger.info("Order sent to the http://localhost:8082");
        return responseEntity.getBody();
    }

    private List<Long> getRandomProducts(List<ProductDTO> productDTOS){
        List<Long> selectedProducts = new ArrayList<>();
        Random random = new Random();
        int randomNrOfProducts = random.nextInt(productDTOS.size() + 1);
        for(int i=0;i<randomNrOfProducts;i++){
            Long id = productDTOS.get(random.nextInt(productDTOS.size()-1)).getId();
            if(!selectedProducts.contains(id))
                selectedProducts.add(id);
        }
       return selectedProducts;
    }
}
