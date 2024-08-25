package com.example.faewoomall.service;

import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.JoinDTO;
import com.example.faewoomall.dto.OAuth2JoinDTO;
import com.example.faewoomall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@RequiredArgsConstructor
@Slf4j
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public void join(JoinDTO joinDTO, BindingResult bindingResult) {

        Boolean isUSer = userRepository.existsByEmail(joinDTO.getEmail());
        if (isUSer) {
            //id 중복
            bindingResult.reject("duplicationId");
            log.info("id 중복");
            return;
        } else if (!joinDTO.getPassword().equals(joinDTO.getCheckPassword())) {
            //비밀번호 확인 불일치
            bindingResult.reject("notEqualPassword");
            log.info("비밀번호 확인 불일치");
            return;
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return;
        }



        User user = new User();
        user.setName(joinDTO.getName());
        user.setUserId(joinDTO.getUserId());
        user.setEmail(joinDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        user.setRole("ROLE_USER");
        user.setZipcode(joinDTO.getZipcode());
        user.setStreetAdr(joinDTO.getStreetAdr());
        user.setDetailAdr(joinDTO.getDetailAdr());

        userRepository.save(user);
        log.info("저장");
    }

    @Transactional
    public void oAuth2Join(OAuth2JoinDTO oAuth2JoinDTO, User user) {

        user.setName(oAuth2JoinDTO.getName());
        user.setZipcode(oAuth2JoinDTO.getZipcode());
        user.setStreetAdr(oAuth2JoinDTO.getStreetAdr());
        user.setDetailAdr(oAuth2JoinDTO.getDetailAdr());
    }
}
