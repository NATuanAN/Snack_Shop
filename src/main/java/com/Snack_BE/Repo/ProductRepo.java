package com.Snack_BE.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Snack_BE.Model.ProductEntity;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity, Long> {

}
