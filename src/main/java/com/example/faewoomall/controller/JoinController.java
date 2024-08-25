package com.example.faewoomall.controller;

import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.CustomOAuth2User;
import com.example.faewoomall.dto.JoinDTO;
import com.example.faewoomall.dto.OAuth2JoinDTO;
import com.example.faewoomall.repository.UserRepository;
import com.example.faewoomall.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class JoinController {

    private final JoinService joinService;
    private final UserRepository userRepository;

    @GetMapping("/join")
    public String joinP(Model model) {
        model.addAttribute("joinDTO", new JoinDTO());
        log.info("joinController");

        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProc(@Validated @ModelAttribute JoinDTO joinDTO, BindingResult bindingResult, Model model) {
        log.info("joinDTO = {} ", joinDTO.getMailCheck());

        joinService.join(joinDTO, bindingResult);

        log.info("JoinDto.getEmail() = {}", joinDTO.getEmail());

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "join";
        }

        return "redirect:/"; //redirect 안하면 오류남 이유는??
    }

    @PostMapping("/oauth2/joinProc")
    public String oauth2JoinProc(@ModelAttribute OAuth2JoinDTO oAuth2JoinDTO,
                                 @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                 BindingResult bindingResult) {
        String oAuth2Id = customOAuth2User.getOAuth2Id();
        User user = userRepository.findByOauth2Id(oAuth2Id);

        joinService.oAuth2Join(oAuth2JoinDTO, user);

        return "redirect:/";
    }

    @GetMapping("/oAuth2/join")
    public String oAuth2JoinForm(Model model) {
        log.info("/oAuth2/join");

        model.addAttribute("oAuth2JoinDTO", new OAuth2JoinDTO());

        return "oAuth2JoinForm";
    }
}
