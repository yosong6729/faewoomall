package com.example.faewoomall.service;

import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.*;
import com.example.faewoomall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User.getAttributes() = {}", oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("clientId  = {}", registrationId);
        OAuth2Response oauth2Response = null;
        log.info("111");
        if (registrationId.equals("naver")) {
            log.info("naver!");
            oauth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            log.info("google!");
            oauth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            oauth2Response = new KakaoResponse(oAuth2User.getAttributes());

        } else {
            return null;
        }

        String oAuth2Id = oauth2Response.getProvider() + " " + oauth2Response.getProviderId();
        User findUser = userRepository.findByOauth2Id(oAuth2Id);

        String role = "ROLE_USER";
        if (findUser == null) {
            User user = new User();
            user.setOauth2Id(oAuth2Id);
            user.setEmail(oauth2Response.getEmail());
            user.setRole(role);

            userRepository.save(user);
        } else {

            findUser.setOauth2Id(oAuth2Id);
            findUser.setEmail(oauth2Response.getEmail());

            role = findUser.getRole();
            userRepository.save(findUser);
        }

        return new CustomOAuth2User(oauth2Response, role);
    }
}
