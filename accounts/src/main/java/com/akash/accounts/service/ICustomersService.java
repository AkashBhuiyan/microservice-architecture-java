package com.akash.accounts.service;

import com.akash.common.dto.CustomerDetailsDto;

/**
 * Author: akash
 * Date: 10/8/24
 */

public interface ICustomersService {

    CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);
}
