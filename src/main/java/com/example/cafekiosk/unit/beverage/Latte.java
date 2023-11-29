package com.example.cafekiosk.unit.beverage;

import com.example.cafekiosk.unit.beverage.Beverage;

public class Latte implements Beverage {
    @Override
    public String getName() {
        return "Latte";
    }

    @Override
    public int getPrice() {
        return 4500;
    }
}
