package com.Snack_BE.Repo;

import org.springframework.stereotype.Repository;
import com.Snack_BE.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {

}
