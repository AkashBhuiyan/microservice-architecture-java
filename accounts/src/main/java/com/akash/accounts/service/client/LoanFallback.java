package com.akash.accounts.service.client;

import com.akash.common.dto.LoanDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/***
 * Akash Bhuiyan, 21/10/24
 **/
@Component
public class LoanFallback implements LoanFeignClient{

    @Override
    public ResponseEntity<LoanDto> fetchLoanDetails(String correlationId, String mobileNumber) {
        return null;
    }
}