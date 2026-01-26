package fr.but3.controller;

import fr.but3.service.MailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MailTestController {
    private final MailService mailService;

    public MailTestController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/test-mail")
    @ResponseBody
    public String testMail() {
        mailService.sendSafe("email@gmail.com", "Test BookTonDJ", "Hello!");
        return "OK (check logs + inbox/spam)";
    }
}