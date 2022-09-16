package com.cdl.services;

public interface CheckoutService {
    void scan(String sku);
    int getRunningTotal();
    int getFinalTotal();
}
