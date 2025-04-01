package com.example.passwordgenerator.controller;

import com.example.passwordgenerator.entity.PasswordEntry;
import com.example.passwordgenerator.service.PasswordEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/password/entries")
public class PasswordEntryController {

    private final PasswordEntryService passwordEntryService;

    public PasswordEntryController(PasswordEntryService passwordEntryService) {
        this.passwordEntryService = passwordEntryService;
    }

    @GetMapping
    public List<PasswordEntry> getAll() {
        return passwordEntryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasswordEntry> getById(@PathVariable Long id) {
        return passwordEntryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PasswordEntry create(@RequestBody PasswordEntry entry) {
        return passwordEntryService.create(entry);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PasswordEntry> update(@PathVariable Long id, @RequestBody PasswordEntry entry) {
        entry.setId(id);
        PasswordEntry updated = passwordEntryService.update(entry);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        passwordEntryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Новый эндпоинт для проверки пароля с дополнительным параметром "check"
    // Пример запроса: GET /api/password/entries/verify?id=1&plainPassword=123456&check=1
    // Если check=0, проверка не производится, если check=1 – производится проверка с хэшированием
    @GetMapping("/verify")
    public ResponseEntity<String> verifyPassword(@RequestParam Long id,
                                                 @RequestParam String plainPassword,
                                                 @RequestParam int check) {
        if (check == 0) {
            return ResponseEntity.ok("Проверка не выполнена, так как параметр check равен 0");
        } else if (check == 1) {
            try {
                boolean isMatch = passwordEntryService.verifyPassword(id, plainPassword);
                return ResponseEntity.ok(isMatch ? "Пароль совпадает" : "Пароль не совпадает");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().body("Неверное значение параметра check. Используйте 0 или 1.");
        }
    }
}