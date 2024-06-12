package com.akash.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/***
 * akash.bhuiyan, 6/5/2024
 **/

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomerAlreadyExistsException extends  RuntimeException{

    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}
