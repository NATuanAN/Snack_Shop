package com.Snack_BE.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Snack_BE.Model.OrderItemEntity;
import com.Snack_BE.config.OrderItemID;

public interface OrderItemRepo extends JpaRepository<OrderItemEntity, OrderItemID> {

}
