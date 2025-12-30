package com.Snack_BE.Model;

import jakarta.persistence.*;
import com.Snack_BE.config.WishlistItemId;

@Entity
@Table(name = "wishlistitem")
@IdClass(WishlistItemId.class)
public class WishlistItem {

    @Id
    @ManyToOne
    @JoinColumn(name = "wishlistid")
    private Wishlist wishlist;

    @Id
    @ManyToOne
    @JoinColumn(name = "productid")
    private ProductEntity product;
}
