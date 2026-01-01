package com.Snack_BE.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Snack_BE.Model.WishlistEntity;

public interface WishListRepo extends JpaRepository<WishlistEntity, Long> {

}
