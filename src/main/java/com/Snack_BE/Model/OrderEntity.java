package com.Snack_BE.Model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
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
    private long oderID;

    @NotBlank
    @Size(max = 255)
    @Column(name = "shippingaddress", nullable = false)
    private String shippingAddress;

    @NotBlank
    @Size(max = 100)
    @Column(name = "paymentmethod", nullable = false)
    private String paymentMethod;

    @CreationTimestamp
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;

    @NotBlank
    @Size(max = 50)
    private String status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "buyerid")
    private UserEntity UserEntity;
}
