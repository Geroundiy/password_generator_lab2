package com.example.passwordgenerator.service;

import com.example.passwordgenerator.entity.PasswordEntry;
import com.example.passwordgenerator.repository.PasswordEntryRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PasswordEntryService {

    private final PasswordEntryRepository passwordEntryRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PasswordEntryService(PasswordEntryRepository passwordEntryRepository) {
        this.passwordEntryRepository = passwordEntryRepository;
    }

    public List<PasswordEntry> findAll() {
        return passwordEntryRepository.findAll();
    }

    public Optional<PasswordEntry> findById(Long id) {
        return passwordEntryRepository.findById(id);
    }

   
    public Optional<PasswordEntry> findByOwner(String owner) {
        return passwordEntryRepository.findByOwner(owner);
    }

    public PasswordEntry create(PasswordEntry entry) {
        
        String plainPassword = entry.getPassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);
        entry.setPassword(hashedPassword);
        return passwordEntryRepository.save(entry);
    }

    public PasswordEntry update(PasswordEntry entry) {
       
        String plainPassword = entry.getPassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);
        entry.setPassword(hashedPassword);
        return passwordEntryRepository.save(entry);
    }

    public void delete(Long id) {
        passwordEntryRepository.deleteById(id);
    }


    public boolean verifyPassword(Long id, String plainPassword) {
        Optional<PasswordEntry> entryOpt = passwordEntryRepository.findById(id);
        if (entryOpt.isPresent()) {
            String storedHashedPassword = entryOpt.get().getPassword();
            return passwordEncoder.matches(plainPassword, storedHashedPassword);
        } else {
            throw new IllegalArgumentException("Запись с указанным ID не найдена");
        }
    }
}
