package com.Snack_BE.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "shop")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopid", nullable = false, unique = true)
    private Long shopId;
    @NotBlank
    @Size(max = 150)
    @Column(name = "shopname", nullable = false, unique = true)
    private String shopName;
    @Size(max = 250)
    @NotBlank
    @Column(name = "address", nullable = false)
    private String address;
    private String description;
    @Size(max = 255)
    @NotBlank
    private String logo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerid",nullable = false)
    @JsonIgnore
    private UserEntity user;
}