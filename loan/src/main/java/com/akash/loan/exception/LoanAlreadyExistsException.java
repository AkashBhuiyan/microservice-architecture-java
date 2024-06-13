package com.akash.loan.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Author: Akash Bhuiyan
 * User:akashbhuiyan, Date:6/13/24
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoanAlreadyExistsException extends RuntimeException {

    public LoanAlreadyExistsException(String message){
        super(message);
    }

}
