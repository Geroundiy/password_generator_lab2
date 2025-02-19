package com.example.passwordgenerator;

import com.example.passwordgenerator.service.PasswordService;
import java.util.Scanner;

public class PasswordGeneratorApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PasswordService passwordService = new PasswordService();

        System.out.println("🔐 Генератор паролей 🔐");
        System.out.print("Введите длину пароля: ");
        int length = scanner.nextInt();

        System.out.print("Введите уровень сложности (1 - цифры, 2 - буквы+цифры, 3 - символы+буквы+цифры): ");
        int complexity = scanner.nextInt();

        String password = passwordService.generatePassword(length, complexity);
        System.out.println("\n✅ Ваш сгенерированный пароль: " + password);
    }
}
