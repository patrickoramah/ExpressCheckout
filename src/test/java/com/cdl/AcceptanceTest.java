package com.cdl;

import com.cdl.model.Item;
import com.cdl.model.PricingRule;
import com.cdl.repositories.ItemRepository;
import com.cdl.repositories.ItemRepositoryImpl;
import com.cdl.services.CheckoutService;
import com.cdl.services.CheckoutServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

public class AcceptanceTest {
    private final CheckoutService checkoutService;
    private final String ITEM_A = "A";
    private final String ITEM_B = "B";
    private final String ITEM_C = "C";
    private final String ITEM_D = "D";

    AcceptanceTest() {
        ItemRepository itemRepository = new ItemRepositoryImpl();
        itemRepository.add(new Item(ITEM_A, 50));
        itemRepository.add(new Item(ITEM_B, 30));
        itemRepository.add(new Item(ITEM_C, 20));
        itemRepository.add(new Item(ITEM_D, 15));

        Map<String, PricingRule> pricingRules = new HashMap<>();
        pricingRules.put("A", new PricingRule(3, 130));
        pricingRules.put("B", new PricingRule(2, 45));

        checkoutService = new CheckoutServiceImpl(itemRepository, pricingRules);
    }

    @Test
    void performCheckoutNoDiscount() {
        // When
        checkoutService.scan(ITEM_A);
        checkoutService.scan(ITEM_B);
        checkoutService.scan(ITEM_C);
        checkoutService.scan(ITEM_D);

        //Then
        assertThat(checkoutService.getRunningTotal()).isEqualTo(115);
        assertThat(checkoutService.getFinalTotal()).isEqualTo(115);
    }

    @Test
    void performCheckoutWithDiscount() {
        // When
        checkoutService.scan(ITEM_A);
        checkoutService.scan(ITEM_A);
        checkoutService.scan(ITEM_A);
        checkoutService.scan(ITEM_B);
        checkoutService.scan(ITEM_B);
        checkoutService.scan(ITEM_C);
        checkoutService.scan(ITEM_D);

        //Then
        assertThat(checkoutService.getRunningTotal()).isEqualTo(245);
        assertThat(checkoutService.getFinalTotal()).isEqualTo(210);
    }
}
