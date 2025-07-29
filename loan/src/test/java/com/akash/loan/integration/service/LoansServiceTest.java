package com.akash.loan.integration.service;

import com.akash.loan.dto.LoansDto;
import com.akash.loan.entity.Loans;
import com.akash.loan.exception.LoanAlreadyExistsException;
import com.akash.loan.exception.ResourceNotFoundException;
import com.akash.loan.repository.LoansRepository;
import com.akash.loan.service.ILoansService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: akash
 * Date: 29/7/25
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("it")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoansServiceTest {

    @Autowired
    private ILoansService loansService;

    @Autowired
    private LoansRepository loansRepository;

    private final String mobileNumber = "01712345678";

    @BeforeEach
    void setUp() {
        loansRepository.deleteAll();
    }

    @Test
    @Order(1)
    void testCreateLoan_Success() {
        loansService.createLoan(mobileNumber);
        Loans loan = loansRepository.findByMobileNumber(mobileNumber).orElse(null);
        assertNotNull(loan);
    }

    @Test
    @Order(2)
    void testCreateLoan_AlreadyExists() {
        loansService.createLoan(mobileNumber);
        assertThrows(LoanAlreadyExistsException.class, () -> loansService.createLoan(mobileNumber));
    }

    @Test
    @Order(3)
    void testFetchLoan_Success() {
        loansService.createLoan(mobileNumber);
        LoansDto loanDto = loansService.fetchLoan(mobileNumber);
        assertEquals(mobileNumber, loanDto.getMobileNumber());
    }

    @Test
    @Order(4)
    void testUpdateLoan_Success() {
        loansService.createLoan(mobileNumber);
        LoansDto loanDto = loansService.fetchLoan(mobileNumber);

        loanDto.setAmountPaid(30000);
        loanDto.setOutstandingAmount(70000);

        boolean updated = loansService.updateLoan(loanDto);
        assertTrue(updated);
    }

    @Test
    @Order(5)
    void testDeleteLoan_Success() {
        loansService.createLoan(mobileNumber);
        assertTrue(loansService.deleteLoan(mobileNumber));
    }

    @Test
    @Order(6)
    void testDeleteLoan_NotFound() {
        assertThrows(ResourceNotFoundException.class, () -> loansService.deleteLoan("00000000000"));
    }
}
