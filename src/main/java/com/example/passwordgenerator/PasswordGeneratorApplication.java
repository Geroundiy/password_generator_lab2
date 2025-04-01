package com.example.passwordgenerator;

import com.example.passwordgenerator.entity.PasswordEntry;
import com.example.passwordgenerator.service.PasswordEntryService;
import com.example.passwordgenerator.service.PasswordService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class PasswordGeneratorApplication implements CommandLineRunner {

    private final PasswordService passwordService;
    private final PasswordEntryService passwordEntryService;

    public PasswordGeneratorApplication(PasswordService passwordService, PasswordEntryService passwordEntryService) {
        this.passwordService = passwordService;
        this.passwordEntryService = passwordEntryService;
    }

    public static void main(String[] args) {
        SpringApplication.run(PasswordGeneratorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("🔐 Генератор паролей (Консольный режим) 🔐");

        System.out.print("Введите ваше имя: ");
        String owner = scanner.nextLine();

        System.out.print("Введите длину пароля (от 4 до 30): ");
        int length = scanner.nextInt();

        System.out.print("Введите уровень сложности (1 - цифры, 2 - цифры+буквы, 3 - цифры+буквы+символы): ");
        int complexity = scanner.nextInt();

        if (length < 4 || length > 30) {
            System.out.println("Ошибка: Длина пароля должна быть от 4 до 30 символов.");
            return;
        }
        if (complexity < 1 || complexity > 3) {
            System.out.println("Ошибка: Уровень сложности должен быть от 1 до 3.");
            return;
        }

        String password = passwordService.generatePassword(length, complexity);
        PasswordEntry entry = new PasswordEntry(password, owner);
        // Сохраняем пароль в БД и получаем объект с присвоенным ID
        entry = passwordEntryService.create(entry);

        System.out.println("\n✅ Ваш сгенерированный пароль: " + password);

        // Новый пункт меню для проверки пароля с БД
        System.out.println("\nПроверить пароль с БД? (0 - нет, 1 - да): ");
        int check = scanner.nextInt();
        if (check == 1) {
            scanner.nextLine(); // очистка буфера после nextInt()
            System.out.print("Введите пароль для проверки: ");
            String checkPassword = scanner.nextLine();
            boolean match = passwordEntryService.verifyPassword(entry.getId(), checkPassword);
            if (match) {
                System.out.println("Пароль совпадает с тем, что хранится в БД.");
            } else {
                System.out.println("Пароль не совпадает с тем, что хранится в БД.");
            }
        } else {
            System.out.println("Проверка пароля не выполнена.");
        }
    }
}