package com.Snack_BE.Model;

import lombok.*;
import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity(name = "product")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;
    @NotBlank
    private String product_name;
    private String description;
    @NotNull
    @Min(10000)
    private BigDecimal price;
    private String image_url;
    @Builder.Default
    private int stock = 0;
    @Builder.Default
    private String status = "active";
    // @ManyToOne
    // private Category category;

    // @ManyToOne
    // private Shop shop;
}
