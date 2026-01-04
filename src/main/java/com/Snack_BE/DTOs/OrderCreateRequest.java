package com.Snack_BE.DTOs;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {
    private Long userId;
    private String shippingAddress;
    private List<Map<String, Long>> items;

    public OrderCreateRequest(List<Map<String, Long>> items, Long userId, String shippingAddress) {
        this.items = items;
        this.userId = userId;
        this.shippingAddress = shippingAddress;
    }
}
