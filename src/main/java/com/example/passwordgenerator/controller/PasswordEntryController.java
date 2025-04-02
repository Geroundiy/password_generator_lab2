package com.example.passwordgenerator.controller;

import com.example.passwordgenerator.entity.PasswordEntry;
import com.example.passwordgenerator.service.PasswordEntryService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/verify")
    public ResponseEntity<String> verifyPassword(@RequestParam Long id,
                                                 @RequestParam String plainPassword) {
        try {
            boolean isMatch = passwordEntryService.verifyPassword(id, plainPassword);
            return ResponseEntity.ok(isMatch ? "Пароль совпадает" : "Пароль не совпадает");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Запись с указанным ID не найдена");
        }
    }
}