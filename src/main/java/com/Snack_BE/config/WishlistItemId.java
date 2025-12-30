package com.Snack_BE.config;

import java.io.Serializable;
import java.util.Objects;

public class WishlistItemId implements Serializable {

    private Long wishlist;
    private Long product;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof WishlistItemId))
            return false;
        WishlistItemId that = (WishlistItemId) o;
        return Objects.equals(wishlist, that.wishlist)
                && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wishlist, product);
    }
}