package com.example.passwordgenerator.controller;

import com.example.passwordgenerator.entity.PasswordEntry;
import com.example.passwordgenerator.service.PasswordEntryService;
import com.example.passwordgenerator.service.PasswordService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    private final PasswordService passwordService;
    private final PasswordEntryService passwordEntryService;

    public PasswordController(PasswordService passwordService, PasswordEntryService passwordEntryService) {
        this.passwordService = passwordService;
        this.passwordEntryService = passwordEntryService;
    }

    // Генерация пароля
    @GetMapping("/generate")
    public String generatePassword(@RequestParam int length,
                                   @RequestParam int complexity,
                                   @RequestParam(required = false, defaultValue = "unknown") String owner) {
        if (length < 4 || length > 30) {
            return "Ошибка: Длина пароля должна быть от 4 до 30 символов.";
        }
        if (complexity < 1 || complexity > 3) {
            return "Ошибка: Уровень сложности должен быть от 1 до 3.";
        }
        String password = passwordService.generatePassword(length, complexity);
        PasswordEntry entry = new PasswordEntry(password, owner);
        passwordEntryService.create(entry);
        return "✅ Ваш сгенерированный пароль: " + password;
    }

    // Проверка пароля
    @GetMapping("/verify")
    public String verifyPassword(@RequestParam String owner, @RequestParam String password) {
        Optional<PasswordEntry> entryOpt = passwordEntryService.findByOwner(owner);
        if (entryOpt.isEmpty()) {
            return "❌ Ошибка: Пользователь не найден.";
        }

        PasswordEntry entry = entryOpt.get();
        // Используем entry, а не опечатку "enary"
        if (passwordService.verifyPassword(password, entry.getPassword())) {
            return "✅ Пароль верен.";
        } else {
            return "❌ Неверный пароль.";
        }
    }
}