package com.akash.loan.dto;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * Author: Akash Bhuiyan
 * User:akashbhuiyan, Date:6/30/24
 */

@ConfigurationProperties(prefix = "loan")
public record LoanContactInfoDto(String message, Map<String, String> contactDetails, List<String> onCallSupport) {
}
