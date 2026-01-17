package com.Snack_BE.config;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.Snack_BE.util.JwtUtil;
import com.Snack_BE.Repo.UserRepo;
import com.Snack_BE.Model.UserEntity;
import com.Snack_BE.Model.UserEntity.AccountType;;

@Component
public class OAuth2SuccessHandler
                extends SimpleUrlAuthenticationSuccessHandler {

        private final JwtUtil jwtUtil;
        private final UserRepo userRepository;

        public OAuth2SuccessHandler(JwtUtil jwtUtil,
                        UserRepo userRepository) {
                this.jwtUtil = jwtUtil;
                this.userRepository = userRepository;
        }

        @Override
        public void onAuthenticationSuccess(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Authentication authentication) throws IOException {

                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

                String email = oAuth2User.getAttribute("email");
                UserEntity user = userRepository.findByEmail(email)
                                .orElseGet(() -> {
                                        UserEntity u = new UserEntity();
                                        u.setEmail(email);
                                        u.setAccounttype(AccountType.Buyer);
                                        u.setName("Name of user");
                                        u.setPassword("Temp");
                                        return userRepository.save(u);
                                });

                String token = jwtUtil.generateToken(
                                user.getEmail(),
                                user.getAccounttype().toString(),
                                user.getUserID());

                response.sendRedirect(
                                "http://localhost:5173/oauth2/success?token=" + token);
        }
}
