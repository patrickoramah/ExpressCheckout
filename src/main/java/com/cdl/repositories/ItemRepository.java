package com.cdl.repositories;

import com.cdl.model.Item;

import java.util.Map;
import java.util.Optional;

public interface ItemRepository {
    void add(Item item);
    Optional<Item> findBySKU(String sku);
    Map<String, Item> getItems();
}
