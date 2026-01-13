package com.Snack_BE.DTOs;

import java.util.List;
import java.util.Map;

import com.Snack_BE.Model.OrderEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTOtoKafka {
    private OrderEntity orderEntity;
    private List<Map<String, Long>> items;

    public OrderDTOtoKafka(List<Map<String, Long>> items, OrderEntity orderEntity) {
        this.items = items;
        this.orderEntity = orderEntity;
    }
}
