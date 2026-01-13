package com.Snack_BE.Model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "order_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    public enum PaymentMethod {
        MOMO,
        VNPAY
    }

    public enum OrderStatus {
        ORDERED,
        PAYMENT_PENDING,
        PAID,
        FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "orderid", nullable = false, columnDefinition = "UUID")
    private UUID orderID;

    @Column(name = "shippingaddress", nullable = false, length = 255)
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentmethod", nullable = false, length = 100)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyerid", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private UserEntity userEntity;
}
