package com.example.faewoomall.controller;

import com.example.faewoomall.dto.MailDTO;
import com.example.faewoomall.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MailController {

    private final MailService mailService;
    private int number;

    /**
     * 인증번호 보내기
     */
    @PostMapping("/mailSend")
    public ResponseEntity<HashMap<String, Object>> mailSend(@RequestBody MailDTO mailDTO) {
        log.info("mailSendMethod");
        HashMap<String, Object> map = new HashMap<>();
        try {
            number = mailService.sendMail(mailDTO.getMail());
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("fail", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return ResponseEntity.ok(map);
    }

    /**
     * 인증번호 일치 확인 여부
     */
    @GetMapping("/mailCheck")
    public ResponseEntity<?> mailCheck(@RequestParam String userNumber) {
        boolean isMatch = userNumber.equals(String.valueOf(number));

        return ResponseEntity.ok(isMatch);
    }


}
