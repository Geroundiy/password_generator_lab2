
# Генератор паролей

## Описание

Приложение "Генератор паролей" предназначено для создания безопасных паролей с учетом заданной пользователем длины и уровня сложности. Сгенерированные пароли хэшируются с использованием алгоритма **BCrypt** и сохраняются в базу данных **PostgreSQL** в таблице `password_entries` вместе с информацией о владельце (имя пользователя) и датой создания. Приложение поддерживает два режима работы:

- **Консольный режим**: Пользователь вводит параметры через консоль, после чего генерируется пароль. Также доступна функция проверки пароля, позволяющая сравнить введенный пароль с хранящимся в базе данных.
- **REST API**: Генерация паролей и выполнение CRUD-операций над сущностями через HTTP-запросы.

## Функциональные возможности

Приложение предоставляет следующие возможности:

- **Генерация паролей трех уровней сложности**:
  - **Уровень 1**: Только цифры.
  - **Уровень 2**: Цифры и буквы.
  - **Уровень 3**: Цифры, буквы и специальные символы.
- **Хэширование паролей**: Перед сохранением в базу данных пароли хэшируются с использованием **BCrypt** для обеспечения безопасности.
- **CRUD-операции для сущностей**:
  - `PasswordEntry`: Создание, чтение, обновление и удаление записей о паролях.
  - `Tag`: Реализация связи многие-ко-многим с `PasswordEntry` и выполнение CRUD-операций над тегами.
- **Проверка пароля**: Сравнение введенного пароля с хранящимся в базе данных с использованием хэширования **BCrypt**.

## Требования

Для работы приложения необходимы следующие компоненты:

- **Java**: Версия 17 или выше.
- **PostgreSQL**: Для хранения данных.
- **Maven**: Для сборки и управления зависимостями.
- **Spring Boot**: Версия 3 или выше.
- **Spring Security Core**: Для реализации хэширования **BCrypt**.
- **SonarCloud**: Проект настроен на 0 bugs и 0 code smells.
- **Переменная окружения `DB_PASSWORD`**: Необходимо задать пароль для подключения к базе данных через переменную окружения для обеспечения безопасности.

## Установка и настройка

Чтобы установить и запустить приложение, выполните следующие шаги:

1. **Клонируйте репозиторий**:
   ```bash
   git clone <URL_репозитория>
   ```
2. **Перейдите в директорию проекта**:
   ```bash
   cd password_generator_lab2
   ```
3. **Настройте переменную окружения `DB_PASSWORD`**:
   - Задайте переменную окружения `DB_PASSWORD` с паролем для пользователя базы данных. Способы задания переменной зависят от вашей операционной системы:
     - **Windows (cmd)**:
       ```cmd
       set DB_PASSWORD=your_password
       ```
     - **PowerShell**:
       ```powershell
       $env:DB_PASSWORD = "your_password"
       ```
     - **Unix-подобные системы (Linux/macOS)**:
       ```bash
       export DB_PASSWORD=your_password
       ```
     - **IntelliJ IDEA**: Добавьте переменную в конфигурацию запуска (Run Configuration → Environment Variables).

4. **Отредактируйте файл `application.properties`**:
   Откройте файл `src/main/resources/application.properties` и укажите параметры подключения к базе данных, включая использование переменной окружения для пароля:
   ```properties
   spring.datasource.url=jdbc:postgresql://<host>:<port>/<имя_базы>
   spring.datasource.username=<пользователь>
   spring.datasource.password=${DB_PASSWORD}
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   ```
   **Пример**:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/passworddb
   spring.datasource.username=postgres
   spring.datasource.password=${DB_PASSWORD}
   ```

5. **Соберите и запустите проект через Maven**:
   ```bash
   mvn spring-boot:run
   ```

## URL-ссылки для работы приложения

После запуска приложение доступно по адресу `http://localhost:8080`. Ниже приведены основные эндпоинты:

### 5.1 Генерация пароля

Генерирует пароль с заданной длиной и уровнем сложности, сохраняя его в базу данных.

- **URL**: `GET /api/password/generate?length={длина}&complexity={уровень}&owner={имя_пользователя}`
- **Параметры**:
  - `length`: Длина пароля (от 4 до 30).
  - `complexity`: Уровень сложности (1 — только цифры, 2 — цифры и буквы, 3 — цифры, буквы и символы).
  - `owner`: Имя владельца (опционально, по умолчанию "unknown").

### 5.2 Проверка пароля (PasswordController)

Проверяет, совпадает ли введенный пароль с хранящимся в базе данных для указанного пользователя.

- **URL**: `GET /api/password/verify?owner={имя_пользователя}&password={введённый_пароль}`

### 5.3 CRUD-операции для PasswordEntry (PasswordEntryController)

- **Получение всех записей**:  
  `GET /api/password/entries`
- **Получение записи по ID**:  
  `GET /api/password/entries/{id}`
- **Создание новой записи**:  
  `POST /api/password/entries`  
  *Тело запроса*: JSON-объект с данными записи.
- **Обнов LENние записи**:  
  `PUT /api/password/entries/{id}`  
  *Тело запроса*: JSON-объект с обновленными данными.
- **Удаление записи**:  
  `DELETE /api/password/entries/{id}`

### 5.4 Проверка пароля с параметром `check` (PasswordEntryController)

Проверяет пароль по ID записи с использованием хэширования.

- **URL**: `GET /api/password/entries/verify?id={id}&plainPassword={пароль}&check=1`
- **Параметры**:
  - `id`: ID записи.
  - `plainPassword`: Пароль для проверки.
  - `check`: 0 или 1 (0 — проверка не производится, 1 — проверка выполняется).

## Пример использования REST API

- **Генерация пароля**:  
  ```
  http://localhost:8080/api/password/generate?length=12&complexity=2&owner=Ivan
  ```
- **Проверка пароля**:  
  ```
  http://localhost:8080/api/password/verify?owner=Ivan&password=введённыйПароль
  ```

## Проверка паролей в консольном режиме

После генерации пароля в консольном режиме приложение предлагает проверить его. Если пользователь выбирает проверку (вводит `1`), ему нужно ввести пароль. Приложение сравнивает введенный пароль с хранящимся в базе данных и выводит результат:
- "Пароль совпадает с тем, что хранится в БД" — при совпадении.
- "Пароль не совпадает с тем, что хранится в БД" — при несовпадении.

## Структура проекта

```
password_generator_lab2/
│-- src/
│ ├── main/
│ │ ├── java/com/example/passwordgenerator/
│ │ │ ├── config/
│ │ │ │ ├── SecurityConfig.java
│ │ │ │ ├── AppConfig.java
│ │ │ ├── controller/
│ │ │ │ ├── PasswordController.java
│ │ │ ├── model/
│ │ │ │ ├── PasswordEntry.java
│ │ │ ├── repository/
│ │ │ │ ├── PasswordRepository.java
│ │ │ ├── service/
│ │ │ │ ├── PasswordService.java
│ │ │ ├── PasswordGeneratorApplication.java
│ ├── resources/
│ │ ├── application.properties
│ │ ├── static/
│ │ ├── templates/
│ ├── test/java/com/example/passwordgenerator/
│ │ ├── PasswordServiceTest.java
│ │ ├── PasswordControllerTest.java
│-- .gitignore
│-- pom.xml
│-- README.md
```
