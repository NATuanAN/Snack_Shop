package com.Snack_BE.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "order_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid", nullable = false)
    private long OrderID;
    @NotEmpty
    @Column(name = "totalamount", nullable = false)
    private float TotalAmount;
    @NotBlank
    @Size(max = 255)
    @Column(name = "shippingaddress", nullable = false)
    private String ShippingAddress;
    @NotBlank
    @Size(max = 100)
    @Column(name = "paymentmethod", nullable = false)
    private String PaymentMethod;
    @NotBlank
    @Size(max = 50)
    private String Status;
    @NotNull
    @OneToOne
    @JoinColumn(name = "buyerid")
    private UserEntity UserEntity;
}
