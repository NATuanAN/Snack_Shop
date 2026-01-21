package com.Snack_BE.Model;

import lombok.*;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid")
    private Long productId;
    @NotBlank
    @Column(name = "productname",unique = true)
    private String productName;
    private String description;
    @NotNull
    @Min(10000)
    private BigDecimal price;
    private String image_url;
    @Builder.Default
    @NotNull
    @Column(name = "stockquantity")
    private int stockQuantity = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopid")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private ShopEntity shop;
}
