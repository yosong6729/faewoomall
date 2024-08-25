package com.example.faewoomall.controller;

import com.example.faewoomall.dto.LoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginP(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());

        return "login";
    }
}
