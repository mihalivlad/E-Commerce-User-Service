package com.example.UserService.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        if(e instanceof ConstraintViolationException){
            StringBuilder message = new StringBuilder("Exceptions: ");
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) e).getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                message.append("\n"+ violation.getMessage().concat(";"));
            }
            return new ResponseEntity<>(message.toString(), HttpStatus.BAD_REQUEST);
        }
        else if(e instanceof DataIntegrityViolationException){
            return new ResponseEntity<>("Username must be unique!", HttpStatus.BAD_REQUEST);
        }
//        else if (e instanceof org.h2.jdbc.JdbcSQLException) {
//            return new ResponseEntity<>("Username must be unique!", HttpStatus.BAD_REQUEST);
//        }
        else{
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
