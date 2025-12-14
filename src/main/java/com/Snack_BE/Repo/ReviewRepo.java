package com.Snack_BE.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Snack_BE.Model.ReviewEnity;

@Repository
public interface ReviewRepo extends JpaRepository<ReviewEnity, Long> {

}
