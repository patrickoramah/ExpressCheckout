package com.cdl.repositories;

import com.cdl.model.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemRepositoryImpl implements ItemRepository {
    Map<String, Item> items = new HashMap<>();

    @Override
    public void add(Item item) {
        items.put(item.getSku(), item);
    }

    @Override
    public Optional<Item> findBySKU(String sku) {
        return Optional.ofNullable(items.getOrDefault(sku, null));
    }

    @Override
    public Map<String, Item> getItems() {
        return items;
    }
}
