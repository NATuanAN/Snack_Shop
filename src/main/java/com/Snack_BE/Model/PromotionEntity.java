package com.Snack_BE.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "promotion")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PromotionEntity {
    public enum PromotionType {
        Shop, Product
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotionid")
    private Long promotionId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private PromotionType type;

    @Column(name = "value", nullable = false, precision = 12, scale = 2)
    private BigDecimal value;

    @Column(name = "startat", nullable = false)
    @CreationTimestamp
    private LocalDateTime startAt;

    @Column(name = "endat", nullable = false)
    @UpdateTimestamp
    private LocalDateTime endAt;

    @ManyToOne
    @JoinColumn(name = "shopid")
    private ShopEntity shop;

    @ManyToOne
    @JoinColumn(name = "productid")
    private ProductEntity product;
}
