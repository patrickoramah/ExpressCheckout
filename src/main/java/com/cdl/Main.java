package com.cdl;


import com.cdl.model.Item;
import com.cdl.model.PricingRule;
import com.cdl.repositories.ItemRepository;
import com.cdl.repositories.ItemRepositoryImpl;
import com.cdl.services.CheckoutService;
import com.cdl.services.CheckoutServiceImpl;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ItemRepository itemRepository = new ItemRepositoryImpl();
        Map<String, PricingRule> pricingRules = new HashMap<>();


        Scanner scanner = new Scanner(System.in);


        System.out.println("Create Items for Store");
        boolean doneUploadingItems = false;
        while(!doneUploadingItems) {
            Item item = new Item();

            System.out.print("Item SKU: ");
            item.setSku(scanner.next());
            
            System.out.print("Item Price: ");
            item.setUnitPrice(scanner.nextInt());

            itemRepository.add(item);

            System.out.print("Upload another item? [Y/N] ");
            doneUploadingItems = scanner.next().equalsIgnoreCase("y");
        }


        System.out.println("Create Pricing rules");
        boolean doneUploadingRules = false;
        while(!doneUploadingRules) {
            System.out.print("Item SKU: ");
            String pricingRuleSKU = scanner.next();

            if (!itemRepository.findBySKU(pricingRuleSKU).isPresent()) {
                System.out.println("Product SKU does not exist. Please try again");
                continue;
            }

            System.out.print("Discount Quantity: ");
            int pricingRuleQuantity = scanner.nextInt();

            System.out.print("Discount Price: ");
            int pricingRulePrice = scanner.nextInt();

            pricingRules.put(pricingRuleSKU, new PricingRule(pricingRuleQuantity, pricingRulePrice));

            System.out.print("Upload another rule? [Y/N] ");
            doneUploadingRules = scanner.next().equalsIgnoreCase("y");
        }


        CheckoutService checkoutService = new CheckoutServiceImpl(itemRepository, pricingRules);
        System.out.println("Scan Items for Store");
        boolean doneScanningItems = false;
        while (!doneScanningItems) {
            try {
                System.out.print("Item SKU: ");
                checkoutService.scan(scanner.next());
                System.out.println("Running Total: " + checkoutService.getRunningTotal());

                System.out.print("Scan another item? [Y/N] ");
                doneScanningItems = scanner.next().equalsIgnoreCase("y");
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("Final Total: " + checkoutService.getFinalTotal());
    }
}