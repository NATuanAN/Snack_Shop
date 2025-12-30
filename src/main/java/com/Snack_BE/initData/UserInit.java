package com.Snack_BE.initData;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Repo.UserRepo;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserInit {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    void init() {
        if (!userRepo.existsByEmail("admin@gmail.com")) {
            UserEntity user = new UserEntity("Admin", "admin@gmail.com", passwordEncoder.encode("123"),
                    com.Snack_BE.Model.UserEntity.AccountType.Admin);
            userRepo.save(user);
        }
    }
}
