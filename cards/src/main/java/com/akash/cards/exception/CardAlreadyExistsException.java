package com.akash.cards.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author: Akash Bhuiyan
 * User:akashbhuiyan, Date:6/12/24, Time:4:42 PM
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CardAlreadyExistsException extends RuntimeException {

    public CardAlreadyExistsException(String message){
        super(message);
    }

}
