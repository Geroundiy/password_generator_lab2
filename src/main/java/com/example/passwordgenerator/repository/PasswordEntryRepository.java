package com.example.passwordgenerator.repository;

import com.example.passwordgenerator.entity.PasswordEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordEntryRepository extends JpaRepository<PasswordEntry, Long> {
    Optional<PasswordEntry> findByOwner(String owner);
}
