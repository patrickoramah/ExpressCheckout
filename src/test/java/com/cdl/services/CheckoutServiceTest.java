package com.cdl.services;

import com.cdl.model.Item;
import com.cdl.model.PricingRule;
import com.cdl.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CheckoutServiceTest {
    private final CheckoutService checkoutService;
    private final ItemRepository itemRepository;
    private final String ITEM_A_SKU = "A";
    private final String ITEM_B_SKU = "B";
    private final String ITEM_C_SKU = "C";

    CheckoutServiceTest() {
        Map<String, PricingRule> pricingRules = new HashMap<>();
        pricingRules.put(ITEM_A_SKU, new PricingRule(3, 130));
        pricingRules.put(ITEM_B_SKU, new PricingRule(2, 45));

        itemRepository = mock(ItemRepository.class);
        checkoutService = new CheckoutServiceImpl(itemRepository, pricingRules);
    }

    @BeforeEach
    void setUp() {
        when(itemRepository.findBySKU(ITEM_A_SKU)).thenReturn(Optional.of(new Item(ITEM_A_SKU, 50)));
        when(itemRepository.findBySKU(ITEM_B_SKU)).thenReturn(Optional.of(new Item(ITEM_B_SKU, 30)));
        when(itemRepository.findBySKU(ITEM_C_SKU)).thenReturn(Optional.of(new Item(ITEM_C_SKU, 20)));
    }

    @Test
    void shouldScanItem() {
        // When
        checkoutService.scan(ITEM_A_SKU);

       // Then
        assertThat(itemRepository.findBySKU(ITEM_A_SKU).isPresent()).isTrue();
    }

    @Test
    void shouldThrowExceptionIfScannedItemDoesNotExist() {
        // When
        String sku = "D";
        assertThatThrownBy(() -> checkoutService
                .scan(sku))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("Item "+sku+" does not exist");
    }

    @Test
    void shouldGetRunningTotal() {
        checkoutService.scan(ITEM_A_SKU);
        checkoutService.scan(ITEM_B_SKU);
        checkoutService.scan(ITEM_C_SKU);

        assertThat(checkoutService.getRunningTotal()).isEqualTo(100);
    }

    @Test
    void shouldGetFinalTotalWithoutDiscount() {
        checkoutService.scan(ITEM_A_SKU);
        checkoutService.scan(ITEM_A_SKU);
        checkoutService.scan(ITEM_B_SKU);
        checkoutService.scan(ITEM_C_SKU);

        assertThat(checkoutService.getFinalTotal()).isEqualTo(150);
    }

    @Test
    void shouldGetFinalTotalWithDiscount() {
        checkoutService.scan(ITEM_A_SKU);
        checkoutService.scan(ITEM_A_SKU);
        checkoutService.scan(ITEM_B_SKU);
        checkoutService.scan(ITEM_B_SKU);
        checkoutService.scan(ITEM_C_SKU);

        assertThat(checkoutService.getFinalTotal()).isEqualTo(165);
    }
}