package com.example.UserService.service;

import com.example.UserService.controller.UserController;
import com.example.UserService.dto.UserDTO;
import com.example.UserService.dto.UserMapper;
import com.example.UserService.model.Address;
import com.example.UserService.model.User;
import com.example.UserService.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Integer addUser(UserDTO userDTO) {
        User user = setUserDetails(userMapper.userDTOtoUser(userDTO));
        userRepository.save(user);
        logger.info("User " + userDTO.getUsername() + " added successfully!");
        return user.getId();
    }

    private User setUserDetails(User user){
        user.setRegistrationDate(new Date(System.currentTimeMillis()));
        if(user.getBillingAddress()==null)
            user.setBillingAddress(user.getDeliveryAddress());
        return user;
    }

    public User updateUser(String username, UserDTO userDTO){
        User actualUser = userRepository.findByUsername(username).orElse(null);
        if(actualUser!=null){
            actualUser = modifyUserFieldsWrapper(actualUser,userDTO);
            userRepository.save(actualUser);
            logger.info("User " + username + " updated successfully!");
        }else {
            logger.info("User " + username + " was not found!");
        }
        return actualUser;
    }


    private User modifyUserFieldsWrapper(User user, UserDTO dto){
        User userUpdate = userMapper.userDTOtoUser(dto);
        String newPassword = userUpdate.getPassword();
        Address newDeliveryAddress = userUpdate.getDeliveryAddress();
        Address newBillingAddress = userUpdate.getBillingAddress();
        return modifyUserFields(user,newPassword,newDeliveryAddress,newBillingAddress);
    }

    private User modifyUserFields(User user,String newPassword,Address newDeliveryAddress,Address newBillingAddress){
        if(newPassword != null)
            user.setPassword(newPassword);
        if(newDeliveryAddress != null) {
            user.setDeliveryAddress(newDeliveryAddress);
            user.setBillingAddress(newDeliveryAddress);
        }
        if(newBillingAddress != null) {
            user.setBillingAddress(newBillingAddress);
        }
        return user;
    }

    public Optional<User> findUser(int id){
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty()) {
            logger.warn("User was not found!");
        }
        return foundUser;
    }

    public Optional<User> findUser(String username){
        Optional<User> foundUser = userRepository.findByUsername(username);
        if(foundUser.isEmpty()){
            logger.warn("User was not found!");
        }
        return foundUser;
    }

    public List<User> findUsers(){
        return userRepository.findAll();
    }

    public Optional<User> deleteUserByUsername(String username){
        Optional<User> foundUser = userRepository.findByUsername(username);
        foundUser.ifPresentOrElse(
                user -> {
                    userRepository.delete(user);
                    logger.info("User " + username + " was deleted successfully!");
                },
                () -> {
                    logger.info("User " + username + " doesn't exist!");
                });

        return foundUser;
    }
}
