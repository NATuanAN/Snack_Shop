package com.Snack_BE.Model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "review")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReviewEnity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewid", updatable = false, insertable = false)
    private long ReviewId;
    private String Content;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer Rating;
    @Column(name = "dateposted", updatable = false, insertable = false)
    private LocalDateTime DatePosted;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "orderid")
    private OrderEntity orderEntity;
}
