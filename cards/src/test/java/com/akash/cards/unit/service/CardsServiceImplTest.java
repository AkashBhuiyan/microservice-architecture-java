package com.akash.cards.unit.service;

import com.akash.cards.constants.CardsConstants;
import com.akash.cards.dto.CardsDto;
import com.akash.cards.entity.Cards;
import com.akash.cards.exception.CardAlreadyExistsException;
import com.akash.cards.exception.ResourceNotFoundException;
import com.akash.cards.repository.CardsRepository;
import com.akash.cards.service.impl.CardsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Author: akash
 * Date: 28/7/25
 */
public class CardsServiceImplTest {

    @Mock
    private CardsRepository cardsRepository;

    @InjectMocks
    private CardsServiceImpl cardsService;

    private Cards cards;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        cards = new Cards();
        cards.setMobileNumber("01712345678");
        cards.setCardNumber("123456789");
        cards.setCardType(CardsConstants.CREDIT_CARD);
        cards.setTotalLimit(5000);
        cards.setAmountUsed(2000);
        cards.setAvailableAmount(1000);
    }

    @Test
    void createCard_ShouldCreateCard_WhenNotExists() {
        when(cardsRepository.findByMobileNumber(cards.getMobileNumber())).thenReturn(Optional.empty());
        when(cardsRepository.save(any(Cards.class))).thenReturn(cards);

        cardsService.createCard(cards.getMobileNumber());

        verify(cardsRepository).save(any(Cards.class));
    }

    @Test
    void createCard_ShouldThrowException_WhenCardExists() {
        when(cardsRepository.findByMobileNumber(cards.getMobileNumber()))
                .thenReturn(Optional.of(cards));

        assertThatThrownBy(() -> cardsService.createCard(cards.getMobileNumber()))
                .isInstanceOf(CardAlreadyExistsException.class)
                .hasMessageContaining("Card already registered");
    }

    @Test
    void fetchCard_ShouldReturnCardDto_WhenExists() {
        when(cardsRepository.findByMobileNumber(cards.getMobileNumber()))
                .thenReturn(Optional.of(cards));

        CardsDto result = cardsService.fetchCard(cards.getMobileNumber());

        assertThat(result).isNotNull();
        assertThat(result.getCardNumber()).isEqualTo(cards.getCardNumber());
    }

    @Test
    void fetchCard_ShouldThrowException_WhenNotExists() {
        when(cardsRepository.findByMobileNumber(cards.getMobileNumber()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardsService.fetchCard(cards.getMobileNumber()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateCard_ShouldUpdateCard_WhenExists() {
        CardsDto cardsDto = new CardsDto();
        cardsDto.setCardNumber(cards.getCardNumber());
        cardsDto.setCardType("Updated");

        when(cardsRepository.findByCardNumber(cardsDto.getCardNumber()))
                .thenReturn(Optional.of(cards));
        when(cardsRepository.save(any(Cards.class))).thenReturn(cards);

        boolean result = cardsService.updateCard(cardsDto);

        assertThat(result).isTrue();
        verify(cardsRepository).save(any(Cards.class));
    }

    @Test
    void updateCard_ShouldThrowException_WhenNotFound() {
        CardsDto cardsDto = new CardsDto();
        cardsDto.setCardNumber("notfound");

        when(cardsRepository.findByCardNumber("notfound")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardsService.updateCard(cardsDto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteCard_ShouldDeleteCard_WhenExists() {
        cards.setCardId(1L);

        when(cardsRepository.findByMobileNumber(cards.getMobileNumber()))
                .thenReturn(Optional.of(cards));

        boolean result = cardsService.deleteCard(cards.getMobileNumber());

        assertThat(result).isTrue();
        verify(cardsRepository).deleteById(cards.getCardId());
    }

    @Test
    void deleteCard_ShouldThrowException_WhenNotFound() {
        when(cardsRepository.findByMobileNumber(cards.getMobileNumber()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardsService.deleteCard(cards.getMobileNumber()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

}
