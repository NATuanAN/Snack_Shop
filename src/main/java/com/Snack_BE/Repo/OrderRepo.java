package com.Snack_BE.Repo;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Snack_BE.Model.OrderEntity;
import com.Snack_BE.Model.UserEntity;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> getByUserEntity(UserEntity userEntity);
}
