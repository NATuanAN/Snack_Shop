package com.Snack_BE.Repo;

import com.Snack_BE.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Snack_BE.Model.ShopEntity;

@Repository
public interface ShopRepo extends JpaRepository<ShopEntity, Long> {
    ShopEntity getShopEntityByUser(UserEntity user);
}
