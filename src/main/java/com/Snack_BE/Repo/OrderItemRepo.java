package com.Snack_BE.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Snack_BE.Model.OrderEntity;
import com.Snack_BE.Model.OrderItemEntity;
import com.Snack_BE.config.OrderItemID;

public interface OrderItemRepo extends JpaRepository<OrderItemEntity, OrderItemID> {
    List<OrderItemEntity> findByOrderEntity(OrderEntity orderEntity);
}
