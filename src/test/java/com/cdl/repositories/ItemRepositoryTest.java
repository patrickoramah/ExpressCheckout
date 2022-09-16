package com.cdl.repositories;

import com.cdl.model.Item;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ItemRepositoryTest {
    private final ItemRepository itemRepository;

    ItemRepositoryTest() {
        itemRepository = new ItemRepositoryImpl();
    }

    @Test
    void canAddItem() {
        //Given
        String itemSKU = "A";
        int itemPrice = 50;
        Item item = new Item(itemSKU, itemPrice);

        //When
        itemRepository.add(item);

        //Then
        assertThat(itemRepository.findBySKU(itemSKU).isPresent()).isTrue();
        assertThat(itemRepository.findBySKU(itemSKU).get().getUnitPrice()).isEqualTo(itemPrice);
    }

    @Test
    void shouldFindItemBySKUIfExist() {
        //Given
        String itemSKU = "A";
        Item item = new Item(itemSKU, 50);

        //When
        itemRepository.add(item);

        //Then
        assertThat(itemRepository.findBySKU(itemSKU).isPresent()).isTrue();
    }

    @Test
    void shouldNotFindItemBySKUIfNotExist() {
        //Then
        assertThat(itemRepository.findBySKU("B").isPresent()).isFalse();
    }

    @Test
    void getItems() {
        //Given
        Item item1 = new Item("A", 50);
        Item item2 = new Item("B", 30);
        Item item3 = new Item("C", 20);

        //When
        itemRepository.add(item1);
        itemRepository.add(item2);
        itemRepository.add(item3);

        // Then
        assertThat(itemRepository.getItems().size()).isEqualTo(3);
    }
}