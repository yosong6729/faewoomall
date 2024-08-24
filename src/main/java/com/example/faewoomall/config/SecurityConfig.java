package com.example.faewoomall.config;

import com.example.faewoomall.handler.CustomOAuth2SuccessHandler;
import com.example.faewoomall.service.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOauth2UserService customOauth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/loginProc","/assets/**", "/join", "/joinProc").permitAll()
                        //static의 /css/**, /js/**, /images/**, /webjars/**, /favicon.*, /*/icon-* 경로 허용
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().permitAll());

        http
                .formLogin((auth) -> auth.loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .usernameParameter("userId")
                        .passwordParameter("password")
                        .permitAll());

        http
                .httpBasic((basic) -> basic.disable());

        //userInfoEndpoint는 OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정을 담당한다.
        //이 때, OAuth2UserService를 통해 userInfo를 얻어오는 요청을 보내게 되는데 이를 커스텀하면 된다.
        http
                .oauth2Login((login) -> login
                        .loginPage("/login")
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOauth2UserService))
                        .successHandler(customOAuth2SuccessHandler));

//        http
//                .csrf((auth) -> auth.disable());

        http
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true));

        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());

        return http.build();
    }


}
