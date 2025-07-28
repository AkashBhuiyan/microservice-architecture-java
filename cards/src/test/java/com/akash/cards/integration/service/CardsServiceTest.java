package com.akash.cards.integration.service;

import com.akash.cards.dto.CardsDto;
import com.akash.cards.repository.CardsRepository;
import com.akash.cards.service.ICardsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/**
 * Author: akash
 * Date: 29/7/25
 */
@ActiveProfiles("it")
@Testcontainers
@SpringBootTest
public class CardsServiceTest {

    @Autowired
    private ICardsService cardsService;

    @Autowired
    private CardsRepository cardsRepository;

    private final String testMobileNumber = "01712345678";

    @BeforeEach
    void setUp() {
        cardsRepository.deleteAll();
    }

    @Test
    void testCreateAndFetchCard() {
        cardsService.createCard(testMobileNumber);

        CardsDto cardsDto = cardsService.fetchCard(testMobileNumber);

        assertThat(cardsDto).isNotNull();
        assertThat(cardsDto.getMobileNumber()).isEqualTo(testMobileNumber);
    }

    @Test
    void testDeleteCard() {
        cardsService.createCard(testMobileNumber);
        boolean deleted = cardsService.deleteCard(testMobileNumber);
        assertThat(deleted).isTrue();
        assertThat(cardsRepository.findByMobileNumber(testMobileNumber)).isEmpty();
    }
}
