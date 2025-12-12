package com.Snack_BE.Model;

import lombok.*;
import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ProductID;
    @NotBlank
    private String ProductName;
    private String Description;
    @NotNull
    @Min(10000)
    private BigDecimal Price;
    private String image_url;
    @Builder.Default
    private int StockQuantity = 0;

    // @ManyToOne
    // private Shop shop;
}
