package com.akash.accounts.unit.service;

import com.akash.accounts.constants.AccountsConstants;
import com.akash.accounts.dto.AccountsDto;
import com.akash.accounts.dto.CustomerDto;
import com.akash.accounts.entity.Accounts;
import com.akash.accounts.entity.Customer;
import com.akash.accounts.exception.CustomerAlreadyExistsException;
import com.akash.accounts.exception.ResourceNotFoundException;
import com.akash.accounts.repository.AccountsRepository;
import com.akash.accounts.repository.CustomerRepository;
import com.akash.accounts.service.impl.AccountsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Author: akash
 * Date: 28/7/25
 */
class AccountsServiceImplTest {

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountsServiceImpl accountsService;

    private CustomerDto customerDto;
    private Customer customer;
    private Accounts account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerDto = new CustomerDto();
        customerDto.setMobileNumber("1234567890");
        customerDto.setName("Akash Bhuiyan");

        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setMobileNumber("1234567890");
        customer.setName("Akash Bhuiyan");

        account = new Accounts();
        account.setAccountNumber(1234567890L);
        account.setCustomerId(1L);
        account.setAccountType(AccountsConstants.SAVINGS);
        account.setBranchAddress(AccountsConstants.ADDRESS);
    }

    @Test
    void testCreateAccount_success() {
        when(customerRepository.findByMobileNumber("1234567890")).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(accountsRepository.save(any(Accounts.class))).thenReturn(account);

        assertDoesNotThrow(() -> accountsService.createAccount(customerDto));

        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(accountsRepository, times(1)).save(any(Accounts.class));
    }

    @Test
    void testCreateAccount_customerAlreadyExists() {
        when(customerRepository.findByMobileNumber("1234567890")).thenReturn(Optional.of(customer));

        assertThrows(CustomerAlreadyExistsException.class, () -> accountsService.createAccount(customerDto));

        verify(customerRepository, never()).save(any(Customer.class));
        verify(accountsRepository, never()).save(any(Accounts.class));
    }

    @Test
    void testFetchAccount_success() {
        when(customerRepository.findByMobileNumber("1234567890")).thenReturn(Optional.of(customer));
        when(accountsRepository.findByCustomerId(1L)).thenReturn(Optional.of(account));

        CustomerDto result = accountsService.fetchAccount("1234567890");

        assertNotNull(result);
        assertEquals("1234567890", result.getMobileNumber());
        assertNotNull(result.getAccountsDto());
        assertEquals(account.getAccountNumber(), result.getAccountsDto().getAccountNumber());
    }

    @Test
    void testFetchAccount_customerNotFound() {
        when(customerRepository.findByMobileNumber("1234567890")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountsService.fetchAccount("1234567890"));
    }

    @Test
    void testUpdateAccount_success() {
        AccountsDto accountsDto = new AccountsDto();
        accountsDto.setAccountNumber(1234567890L);
        customerDto.setAccountsDto(accountsDto);

        when(accountsRepository.findById(1234567890L)).thenReturn(Optional.of(account));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountsRepository.save(any(Accounts.class))).thenReturn(account);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        boolean result = accountsService.updateAccount(customerDto);

        assertTrue(result);
        verify(accountsRepository).save(any(Accounts.class));
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void testUpdateAccount_accountNotFound() {
        AccountsDto accountsDto = new AccountsDto();
        accountsDto.setAccountNumber(9999999999L);
        customerDto.setAccountsDto(accountsDto);

        when(accountsRepository.findById(9999999999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountsService.updateAccount(customerDto));
    }

    @Test
    void testDeleteAccount_success() {
        when(customerRepository.findByMobileNumber("1234567890")).thenReturn(Optional.of(customer));

        boolean result = accountsService.deleteAccount("1234567890");

        assertTrue(result);
        verify(accountsRepository).deleteByCustomerId(1L);
        verify(customerRepository).deleteById(1L);
    }

    @Test
    void testDeleteAccount_customerNotFound() {
        when(customerRepository.findByMobileNumber("1234567890")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountsService.deleteAccount("1234567890"));
    }
}
