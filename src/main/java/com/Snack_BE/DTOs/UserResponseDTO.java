package com.Snack_BE.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    public enum AccountType {
        Buyer,
        Seller,
        Admin
    }
    private String name;
    private String email;
    private String phoneNumber;
    private AccountType accounttype = AccountType.Buyer;
}
