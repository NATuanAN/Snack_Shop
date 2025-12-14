package com.Snack_BE.Model;

import jakarta.persistence.Column;
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
    @Column(name = "shopid", nullable = false, unique = true)
    private Long ShopID;
    @NotBlank
    @Size(max = 150)
    @Column(name = "shopname", nullable = false, unique = true)
    private String ShopName;
    @Size(max = 250)
    @NotBlank
    @Column(name = "address", nullable = true)
    private String Address;
    private String Description;
    @Size(max = 255)
    @NotBlank
    private String Logo;

}
