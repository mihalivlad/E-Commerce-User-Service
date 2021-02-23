package com.example.UserService.controller;
import com.example.UserService.dto.ProductDTO;
import com.example.UserService.dto.UserDTO;
import com.example.UserService.model.User;
import com.example.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private UserService userService;
    private ShoppingCartClient shoppingCartClient;
    private ProductClient productClient;

    @Autowired
    public UserController(UserService userService, ShoppingCartClient shoppingCartClient, ProductClient productClient) {
        this.userService = userService;
        this.shoppingCartClient = shoppingCartClient;
        this.productClient = productClient;
    }

    @PostMapping(path = "/")
    public ResponseEntity<Integer> addUser(@RequestBody UserDTO userDTO) {
        Integer userId = userService.addUser(userDTO);
        return new ResponseEntity<>(userId, HttpStatus.CREATED);
    }

    @PutMapping(path = "/")
    public ResponseEntity<User> updateUser(@RequestParam(name = "username") String username, @RequestBody UserDTO userDTO) {
        User userUpdate = userService.updateUser(username,userDTO);
        return new ResponseEntity<>(userUpdate,HttpStatus.OK);
    }

    @GetMapping(path = "/")
    public ResponseEntity<User> findUser(@RequestParam(name = "id", required = false) Integer id, @RequestParam(name = "username", required = false) String username){
        Optional<User> found = Optional.empty();
        if (id != null) {
            found = userService.findUser(id);
        } else if(username != null) {
            found = userService.findUser(username);
        }

        return found.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

    }

    @DeleteMapping(path="/")
    public ResponseEntity<User> deleteUserByUsername(@RequestParam(name = "username") String username){
        Optional<User> found = userService.deleteUserByUsername(username);
        return found.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/users")
    public ResponseEntity<List<User>> findUsers(){
        List<User> users = userService.findUsers();
        if (users.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(path = "/addToCart")
    public ResponseEntity<String> addToCart(@RequestParam String username) {
        Long categoryId = productClient.getCategories();
        if (categoryId == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<ProductDTO> productDTOList = productClient.getProducts(categoryId);
        if (productDTOList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        String response = shoppingCartClient.buyProducts(username, productDTOList);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
