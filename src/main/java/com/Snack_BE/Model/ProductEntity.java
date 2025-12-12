package com.Snack_BE.Model;

import lombok.*;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid")
    private Long ProductID;
    @NotBlank
    @Column(name = "productname")
    private String ProductName;
    private String Description;
    @NotNull
    @Min(10000)
    private BigDecimal Price;
    private String image_url;
    @Builder.Default
    @NotNull
    @Column(name = "stockquantity")
    private int StockQuantity = 0;
    @ManyToOne
    @JoinColumn(name = "shopid")
    private ShopEntity shop;
}
