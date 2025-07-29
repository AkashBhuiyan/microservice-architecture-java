package com.akash.loan.integration.controller;


import com.akash.loan.dto.LoansDto;
import com.akash.loan.repository.LoansRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Author: akash
 * Date: 29/7/25
 */
@ActiveProfiles("it")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoansControllerTest {


    @LocalServerPort
    private int port;

    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String baseUrl = "/api";

    private final String testMobile = "01712345678";

    @BeforeEach
    void cleanDb() {
        loansRepository.deleteAll();
    }

    @Test
    @Order(1)
    void createLoan_success() {
        String url = "http://localhost:" + port + baseUrl + "/create?mobileNumber=" + testMobile;
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(2)
    void fetchLoan_success() {
        // Pre-create a loan
        String createUrl = "http://localhost:" + port + baseUrl + "/create?mobileNumber=" + testMobile;
        restTemplate.postForEntity(createUrl, null, String.class);

        String fetchUrl = "http://localhost:" + port + baseUrl + "/fetch?mobileNumber=" + testMobile;

        HttpHeaders headers = new HttpHeaders();
        headers.set("bank-correlation-id", "test-correlation-id");

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<LoansDto> response = restTemplate.exchange(fetchUrl, HttpMethod.GET, requestEntity, LoansDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMobile, response.getBody().getMobileNumber());
    }

    @Test
    @Order(3)
    void updateLoan_success() {
        // First create loan
        restTemplate.postForEntity("http://localhost:" + port + baseUrl + "/create?mobileNumber=" + testMobile, null, String.class);

        // Fetch loan to get loanNumber
        HttpHeaders headers = new HttpHeaders();
        headers.set("bank-correlation-id", "test");
        ResponseEntity<LoansDto> fetchResponse = restTemplate.exchange(
                "http://localhost:" + port + baseUrl + "/fetch?mobileNumber=" + testMobile,
                HttpMethod.GET, new HttpEntity<>(headers), LoansDto.class);

        LoansDto dto = fetchResponse.getBody();
        dto.setAmountPaid(20000);
        dto.setOutstandingAmount(80000);

        HttpHeaders updateHeaders = new HttpHeaders();
        updateHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoansDto> updateRequest = new HttpEntity<>(dto, updateHeaders);
        ResponseEntity<String> updateResponse = restTemplate.exchange(
                "http://localhost:" + port + baseUrl + "/update",
                HttpMethod.PUT, updateRequest, String.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
    }

    @Test
    @Order(4)
    void deleteLoan_success() {
        restTemplate.postForEntity("http://localhost:" + port + baseUrl + "/create?mobileNumber=" + testMobile, null, String.class);

        String deleteUrl = "http://localhost:" + port + baseUrl + "/delete?mobileNumber=" + testMobile;
        ResponseEntity<String> response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, null, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
