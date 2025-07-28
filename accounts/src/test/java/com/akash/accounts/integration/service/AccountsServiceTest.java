package com.akash.accounts.integration.service;

import com.akash.accounts.dto.CustomerDto;
import com.akash.accounts.service.IAccountsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Author: akash
 * Date: 28/7/25
 */
@ActiveProfiles("it")
@Testcontainers
@SpringBootTest
public class AccountsServiceTest {

    @Autowired
    private IAccountsService accountsService;

    @Test
    void testCreateAndFetch() {
        // Step 1: Create CustomerDto with test data
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Akash Bhuiyan");
        customerDto.setEmail("akash@example.com");
        customerDto.setMobileNumber("5551234567");

        // Step 2: Create account
        accountsService.createAccount(customerDto);

        // Step 3: Fetch account by mobile number
        CustomerDto fetchedDto = accountsService.fetchAccount("5551234567");

        // Step 4: Assertions
        assertNotNull(fetchedDto, "Fetched customer should not be null");
        assertEquals("Akash Bhuiyan", fetchedDto.getName());
        assertEquals("akash@example.com", fetchedDto.getEmail());
        assertEquals("5551234567", fetchedDto.getMobileNumber());

        assertNotNull(fetchedDto.getAccountsDto(), "Account details should not be null");
        assertEquals("Savings", fetchedDto.getAccountsDto().getAccountType()); // Assuming default type is 'Savings'
        assertEquals("10/3 Gulshan 1, Dhaka", fetchedDto.getAccountsDto().getBranchAddress()); // Based on AccountsConstants.ADDRESS
    }

}

