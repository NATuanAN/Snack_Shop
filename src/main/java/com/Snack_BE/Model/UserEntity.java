package com.Snack_BE.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "users_table")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserEntity {
    public enum AccountType {
        Buyer,
        Seller,
        Admin
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserID;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String name;

    @NotBlank
    @Size(max = 150)
    @Column(length = 150, nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(length = 255, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AccountType accounttype = AccountType.Buyer;
}
