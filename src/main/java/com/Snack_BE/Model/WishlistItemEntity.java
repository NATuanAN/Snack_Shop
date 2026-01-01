package com.Snack_BE.Model;

import jakarta.persistence.*;
import com.Snack_BE.config.WishlistItemId;

@Entity
@Table(name = "wishlistitem")
@IdClass(WishlistItemId.class)
public class WishlistItemEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "wishlistid")
    private WishlistEntity wishlist;

    @Id
    @ManyToOne
    @JoinColumn(name = "productid")
    private ProductEntity product;
}
