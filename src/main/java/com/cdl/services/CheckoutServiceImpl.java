package com.cdl.services;

import com.cdl.model.PricingRule;
import com.cdl.model.Item;
import com.cdl.repositories.ItemRepository;

import java.util.HashMap;
import java.util.Map;

public class CheckoutServiceImpl implements CheckoutService {
    private final ItemRepository itemRepository;
    private final Map<String, PricingRule> pricingRules;

    private final Map<Item, Integer> cart;

    public CheckoutServiceImpl(ItemRepository itemRepository, Map<String, PricingRule> pricingRules) {
        this.itemRepository = itemRepository;
        this.pricingRules = pricingRules;
        this.cart = new HashMap<>();
    }

    @Override
    public void scan(String sku) {
        Item item = itemRepository.findBySKU(sku).orElseThrow(() -> new IllegalArgumentException("Item "+sku+" does not exist"));
        cart.put(item, cart.containsKey(item) ? cart.get(item) + 1 : 1);
    }

    @Override
    public int getRunningTotal() {
        // Assumption made is running total is unit price of all items before discount is applied
        int runningTotal = 0;
        for (Map.Entry<Item, Integer> cartItem : cart.entrySet()) {
            runningTotal += cartItem.getKey().getUnitPrice() * cartItem.getValue();
        }
        return runningTotal;
    }

    @Override
    public int getFinalTotal() {
        /* To solve this,
        * I looped through all cart items and checked if the pricing rules contained any offer for the current item
        * Then I got the cart quantity and divided by offer criteria
        * */
        int finalTotal = 0;
        for (Map.Entry<Item, Integer> cartItem : cart.entrySet()) {
            if (pricingRules.containsKey(cartItem.getKey().getSku()) && pricingRules.get(cartItem.getKey().getSku()).getQuantity() > 1) {
                int offerCount = cartItem.getValue() / pricingRules.get(cartItem.getKey().getSku()).getQuantity();
                int normalPriceCount = cartItem.getValue() % pricingRules.get(cartItem.getKey().getSku()).getQuantity();

                if (offerCount >= 1) {
                    finalTotal += offerCount * pricingRules.get(cartItem.getKey().getSku()).getPrice();
                }
                if (normalPriceCount >= 1) {
                    finalTotal += normalPriceCount * cartItem.getKey().getUnitPrice();
                }
            } else {
                finalTotal += cartItem.getValue() * cartItem.getKey().getUnitPrice();
            }
        }
        return finalTotal;
    }
}
