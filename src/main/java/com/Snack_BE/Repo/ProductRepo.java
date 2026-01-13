package com.Snack_BE.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Snack_BE.DTOs.ProductDTO;
import com.Snack_BE.Model.ProductEntity;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity, Long> {
    @Query("""
                select new com.Snack_BE.DTOs.ProductDTO(
                    p.productId,
                    p.productName,
                    p.price,
                    p.image_url,
                    s.shopName
                )
                from product p
                join p.shop s
            """)
    List<ProductDTO> findAllProductDTO();

}
