package com.Snack_BE.Model;

import java.math.BigDecimal;
import com.Snack_BE.config.OrderItemID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @MapsId("orderId")
    @JoinColumn(name = "orderid")
    private OrderEntity orderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @MapsId("productId")
    @JoinColumn(name = "productid")
    private ProductEntity productEntity;

    @Column(nullable = false)
    private Integer Quantity;

    @Column(name = "unitprice")
    private BigDecimal UnitPrice;
}
