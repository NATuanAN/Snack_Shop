package com.Snack_BE.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Snack_BE.Model.OrderEntity;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, Long> {

}
