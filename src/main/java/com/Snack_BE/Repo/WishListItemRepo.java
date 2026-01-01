package com.Snack_BE.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Snack_BE.Model.WishlistItemEntity;
import com.Snack_BE.config.WishlistItemId;

public interface WishListItemRepo extends JpaRepository<WishlistItemEntity, WishlistItemId> {

}
