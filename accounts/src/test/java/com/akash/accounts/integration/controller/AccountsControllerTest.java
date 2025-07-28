package com.akash.accounts.integration.controller;

import com.akash.accounts.dto.AccountsDto;
import com.akash.accounts.dto.CustomerDto;
import com.akash.accounts.dto.ResponseDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Author: akash
 * Date: 28/7/25
 */
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl(String path) {
        return "http://localhost:" + port + "/api" + path;
    }

    @Test
    void testCreateAndFetchAccount() {
        // Prepare customer data
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Akash Bhuiyan");
        customerDto.setEmail("akash@example.com");
        customerDto.setMobileNumber("01712345678");

        // Send POST request to /create
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CustomerDto> request = new HttpEntity<>(customerDto, headers);

        ResponseEntity<ResponseDto> createResponse = restTemplate.postForEntity(
                getBaseUrl("/create"), request, ResponseDto.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertEquals("201", createResponse.getBody().getStatusCode());

        // Send GET request to /fetch
        ResponseEntity<CustomerDto> fetchResponse = restTemplate.getForEntity(
                getBaseUrl("/fetch?mobileNumber=01712345678"), CustomerDto.class
        );

        assertEquals(HttpStatus.OK, fetchResponse.getStatusCode());
        assertNotNull(fetchResponse.getBody());
        assertEquals("Akash Bhuiyan", fetchResponse.getBody().getName());

        AccountsDto accountsDto = fetchResponse.getBody().getAccountsDto();
        assertNotNull(accountsDto);
        assertEquals("Savings", accountsDto.getAccountType());
    }
}

