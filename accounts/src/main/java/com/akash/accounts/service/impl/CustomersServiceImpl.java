package com.akash.accounts.service.impl;

import com.akash.accounts.entity.Accounts;
import com.akash.accounts.entity.Customer;
import com.akash.accounts.exception.ResourceNotFoundException;
import com.akash.accounts.mapper.AccountsMapper;
import com.akash.accounts.mapper.CustomerMapper;
import com.akash.accounts.repository.AccountsRepository;
import com.akash.accounts.repository.CustomerRepository;
import com.akash.accounts.service.ICustomersService;
import com.akash.accounts.service.client.CardsFeignClient;
import com.akash.accounts.service.client.LoanFeignClient;
import com.akash.common.dto.AccountsDto;
import com.akash.common.dto.CardsDto;
import com.akash.common.dto.CustomerDetailsDto;
import com.akash.common.dto.LoanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Author: akash
 * Date: 10/8/24
 */

@Service
@RequiredArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;
    private final CardsFeignClient cardsFeignClient;
    private final LoanFeignClient loanFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoanDto> loansDtoResponseEntity = loanFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        if(null!=loansDtoResponseEntity) {
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        if(null!=cardsDtoResponseEntity) {
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }

        return customerDetailsDto;

    }
}
