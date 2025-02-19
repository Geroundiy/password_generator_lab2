package com.example.passwordgenerator.controller;

import com.example.passwordgenerator.service.PasswordService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("/generate")
    public String generatePassword(@RequestParam int length, @RequestParam int complexity) {
        if (length < 4 || length > 30) {
            return "Ошибка: Длина пароля должна быть от 4 до 30 символов.";
        }
        if (complexity < 1 || complexity > 4) {
            return "Ошибка: Уровень сложности должен быть от 1 до 4.";
        }
        return passwordService.generatePassword(length, complexity);
    }
}
