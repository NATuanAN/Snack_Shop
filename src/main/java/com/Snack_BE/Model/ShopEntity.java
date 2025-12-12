package com.Snack_BE.Model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "shop")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopid;
    @NotBlank
    @Size(max = 150)
    private String shopname;
    @Size(max = 250)
    @NotBlank
    private String address;
    private String description;
    @Size(max = 255)
    @NotBlank
    private String logo;

}
