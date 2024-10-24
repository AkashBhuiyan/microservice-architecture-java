package com.akash.accounts.service.client;

import com.akash.accounts.constants.AccountsConstants;
import com.akash.common.dto.LoanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loan", fallback = LoanFallback.class)
public interface LoanFeignClient {

    @GetMapping(value = "/api/fetch", consumes = "application/json")
    public ResponseEntity<LoanDto> fetchLoanDetails(@RequestHeader(AccountsConstants.BANK_CORRELATION_ID) String correlationId,
                                                    @RequestParam String mobileNumber);

}
