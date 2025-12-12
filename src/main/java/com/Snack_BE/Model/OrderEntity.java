package com.Snack_BE.Model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    private long OrderID;
    private long TotalAmount;
    private String ShippingAddress;
    private String PaymentMethod;
    private String startus;
    private long BuyerID;
}
