package com.Snack_BE.initData;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Repo.UserRepo;

import io.lettuce.core.event.command.CommandListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserInit {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    // void init() {
    // if (!userRepo.existsByEmail("admin@gmail.com")) {
    // UserEntity user = new UserEntity("Admin", "admin@gmail.com",
    // passwordEncoder.encode("123"),
    // com.Snack_BE.Model.UserEntity.AccountType.Admin);
    // userRepo.save(user);
    // }
    // }
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        for (int i = 0; i < 5; i++) {
            try {
                if (!userRepo.existsByEmail("admin@gmail.com")) {
                    UserEntity user = new UserEntity(
                            "Admin",
                            "admin@gmail.com",
                            passwordEncoder.encode("123"),
                            UserEntity.AccountType.Admin);
                    userRepo.save(user);
                }
                break;
            } catch (Exception e) {
                try {
                    Thread.sleep(3000); // đợi DB tỉnh ngủ
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

}
