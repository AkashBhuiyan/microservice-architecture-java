package com.akash.cards.integration.controller;

import com.akash.cards.dto.CardsDto;
import com.akash.cards.repository.CardsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Author: akash
 * Date: 29/7/25
 */
@ActiveProfiles("it")
@SpringBootTest
@AutoConfigureMockMvc
public class CardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String mobileNumber = "01712345678";

    @BeforeEach
    void setup() {
        cardsRepository.deleteAll(); // clean DB before each test
    }

    @Test
    void createCard_ShouldReturn201() throws Exception {
        mockMvc.perform(post("/api/create")
                        .param("mobileNumber", mobileNumber))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value("201"));
    }

    @Test
    void fetchCard_ShouldReturn200() throws Exception {
        mockMvc.perform(post("/api/create").param("mobileNumber", mobileNumber));

        mockMvc.perform(get("/api/fetch")
                        .header("bank-correlation-id", "test-id")
                        .param("mobileNumber", mobileNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mobileNumber").value(mobileNumber));
    }

    @Test
    void updateCard_ShouldReturn200() throws Exception {
        mockMvc.perform(post("/api/create").param("mobileNumber", mobileNumber));
        var card = cardsRepository.findByMobileNumber(mobileNumber).get();

        CardsDto dto = new CardsDto();
        dto.setCardNumber(card.getCardNumber());
        dto.setMobileNumber(mobileNumber);
        dto.setCardType(card.getCardType());
        dto.setTotalLimit(10000);
        dto.setAmountUsed(1000);
        dto.setAvailableAmount(9000);

        mockMvc.perform(put("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }

    @Test
    void deleteCard_ShouldReturn200() throws Exception {
        mockMvc.perform(post("/api/create").param("mobileNumber", mobileNumber));

        mockMvc.perform(delete("/api/delete").param("mobileNumber", mobileNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }

    @Test
    void getBuildInfo_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/build-info"))
                .andExpect(status().isOk());
    }

    @Test
    void getContactInfo_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/api/contact-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").doesNotExist());
    }
}
