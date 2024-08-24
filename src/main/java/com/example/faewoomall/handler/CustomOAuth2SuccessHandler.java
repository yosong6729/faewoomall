package com.example.faewoomall.handler;

import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.CustomOAuth2User;
import com.example.faewoomall.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetails.getOAuth2Id();


        User user = userService.findByEmail(username);

        response.sendRedirect("/oAuth2/join");
    }
}
