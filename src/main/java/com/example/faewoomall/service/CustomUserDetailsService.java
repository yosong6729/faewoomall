package com.example.faewoomall.service;

import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.CustomUserDetails;
import com.example.faewoomall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        User findUser = userRepository.findByUserId(userId);

        if (findUser != null) {

            return new CustomUserDetails(findUser);
        }

        return null;
    }
}
