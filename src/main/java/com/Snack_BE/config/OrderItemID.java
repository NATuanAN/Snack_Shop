package com.Snack_BE.config;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemID implements Serializable {
    private long orderId;
    private long productId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OrderItemID))
            return false;
        OrderItemID object = (OrderItemID) o;
        return Objects.equals(orderId, object.orderId) &&
                Objects.equals(productId, object.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }
}
