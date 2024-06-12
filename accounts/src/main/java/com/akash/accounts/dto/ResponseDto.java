package com.akash.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


/***
 * akash.bhuiyan, 6/5/2024
 **/


/*@Schema(
        name = "Response",
        description = "Schema to hold successful response information"
)*/
@Data @AllArgsConstructor
public class ResponseDto {

    /*@Schema(
            description = "Status code in the response"
    )*/
    private String statusCode;

    /*@Schema(
            description = "Status message in the response"
    )*/
    private String statusMsg;
}
