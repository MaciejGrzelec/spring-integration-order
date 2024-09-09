package com.example.demo.code;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Order {
    private int orderId;
    private String itemName;
    private double amount;
    private String orderStatus;
    private List<String> productList;
}
