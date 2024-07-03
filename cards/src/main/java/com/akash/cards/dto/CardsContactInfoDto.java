package com.akash.cards.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * Author: Akash Bhuiyan
 * User:akashbhuiyan, Date:6/30/24
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "cards")
public class CardsContactInfoDto {
    private String message;
    private Map<String, String> contactDetails;
    private List<String> onCallSupport;
}
