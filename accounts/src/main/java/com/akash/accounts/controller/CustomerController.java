package com.akash.accounts.controller;

import com.akash.accounts.constants.AccountsConstants;
import com.akash.accounts.dto.ErrorResponseDto;
import com.akash.accounts.service.ICustomersService;
import com.akash.common.dto.CustomerDetailsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Author: akash
 * Date: 10/8/24
 */

@Tag(
        name = "REST API for Customers in Bank",
        description = "REST APIs in Bank to FETCH customer details"
)
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final ICustomersService iCustomersService;

    @Operation(
            summary = "Fetch Customer Details REST API",
            description = "REST API to fetch Customer details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(@RequestHeader(AccountsConstants.BANK_CORRELATION_ID) String correlationId,
                                                                    @RequestParam @Pattern(regexp="(^$|[0-9]{11})",
                                                                            message = "Mobile number must be 11 digits")
                                                                                           String mobileNumber) {
        logger.debug("bank correlation id found {}", correlationId);
        logger.debug("fetchCustomerDetails method start");
        CustomerDetailsDto customerDetailsDto = iCustomersService.fetchCustomerDetails(mobileNumber, correlationId);
        logger.debug("fetchCustomerDetails method end");
        return ResponseEntity.status(HttpStatus.SC_OK).body(customerDetailsDto);

    }
}
