package com.akash.loan.unit.service;

import com.akash.loan.constants.LoansConstants;
import com.akash.loan.dto.LoansDto;
import com.akash.loan.entity.Loans;
import com.akash.loan.exception.LoanAlreadyExistsException;
import com.akash.loan.exception.ResourceNotFoundException;
import com.akash.loan.mapper.LoansMapper;
import com.akash.loan.repository.LoansRepository;
import com.akash.loan.service.impl.LoansServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Author: akash
 * Date: 29/7/25
 */
public class LoansServiceImplTest {

    @InjectMocks
    private LoansServiceImpl loansService;

    @Mock
    private LoansRepository loansRepository;

    private Loans loans;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        loans = new Loans();
        loans.setLoanId(1L);
        loans.setLoanNumber("123456789");
        loans.setMobileNumber("01712345678");
        loans.setLoanType(LoansConstants.HOME_LOAN);
        loans.setTotalLoan(100000);
        loans.setAmountPaid(10000);
        loans.setOutstandingAmount(90000);
    }

    @Test
    void createLoan_ShouldSaveLoan_WhenMobileNotRegistered() {
        when(loansRepository.findByMobileNumber("01712345678")).thenReturn(Optional.empty());

        loansService.createLoan("01712345678");

        verify(loansRepository, times(1)).save(any(Loans.class));
    }

    @Test
    void createLoan_ShouldThrowException_WhenLoanAlreadyExists() {
        when(loansRepository.findByMobileNumber("01712345678")).thenReturn(Optional.of(loans));

        assertThatThrownBy(() -> loansService.createLoan("01712345678"))
                .isInstanceOf(LoanAlreadyExistsException.class)
                .hasMessageContaining("Loan already registered");

        verify(loansRepository, never()).save(any(Loans.class));
    }

    @Test
    void fetchLoan_ShouldReturnLoanDto_WhenLoanExists() {
        when(loansRepository.findByMobileNumber("01712345678")).thenReturn(Optional.of(loans));

        LoansDto result = loansService.fetchLoan("01712345678");

        assertThat(result).isNotNull();
        assertThat(result.getMobileNumber()).isEqualTo("01712345678");
    }

    @Test
    void fetchLoan_ShouldThrowException_WhenLoanNotFound() {
        when(loansRepository.findByMobileNumber("01712345678")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loansService.fetchLoan("01712345678"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateLoan_ShouldUpdateSuccessfully_WhenLoanExists() {
        LoansDto dto = LoansMapper.mapToLoansDto(loans, new LoansDto());
        when(loansRepository.findByLoanNumber("123456789")).thenReturn(Optional.of(loans));

        boolean result = loansService.updateLoan(dto);

        assertThat(result).isTrue();
        verify(loansRepository, times(1)).save(any(Loans.class));
    }

    @Test
    void updateLoan_ShouldThrowException_WhenLoanNotFound() {
        LoansDto dto = new LoansDto();
        dto.setLoanNumber("nonexistent");

        when(loansRepository.findByLoanNumber("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loansService.updateLoan(dto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteLoan_ShouldDeleteLoan_WhenExists() {
        when(loansRepository.findByMobileNumber("01712345678")).thenReturn(Optional.of(loans));

        boolean result = loansService.deleteLoan("01712345678");

        assertThat(result).isTrue();
        verify(loansRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteLoan_ShouldThrowException_WhenLoanNotFound() {
        when(loansRepository.findByMobileNumber("01712345678")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loansService.deleteLoan("01712345678"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
