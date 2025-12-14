package com.Snack_BE.Model;

import java.math.BigDecimal;
import com.Snack_BE.config.OrderItemID;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "orderitem")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItemEntity {
    @EmbeddedId
    private OrderItemID orderItemID;
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "orderid")
    private OrderEntity Order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "productid")
    private ProductEntity Product;

    @Column(nullable = false)
    private Integer Quantity;

    @Column(name = "unitprice", nullable = false)
    private BigDecimal UnitPrice;
}
