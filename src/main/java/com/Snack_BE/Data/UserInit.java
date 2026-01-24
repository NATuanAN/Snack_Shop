package com.Snack_BE.Data;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Repo.UserRepo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserInit {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
            try {
                if (!userRepo.existsByEmail("admin@gmail.com")) {
                    UserEntity user = new UserEntity(
                            "Admin",
                            "admin@gmail.com",
                            passwordEncoder.encode("123"),
                            UserEntity.AccountType.Admin,
                            "0908427830",
                            UserEntity.Active.active
                    );
                    userRepo.save(user);
                }
            } catch (Exception e) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }