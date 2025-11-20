# stellarburegers-api-tests — API-автотесты Stellar Burgers

Автоматизированные тесты для API веб-приложения **Stellar Burgers**:  
создание пользователя, авторизация, изменение данных, создание заказов и получение списка заказов

Проект выполнен в рамках учебного задания Яндекс.Практикума

---

## 🚀 Технологии

| Технология        | Версия   |
|------------------|----------|
| Java             | 11       |
| Maven            | 3.x      |
| REST Assured     | 5.3.0    |
| JUnit            | 4.13.2   |
| Allure           | 2.21.0   |
| Gson             | 2.10.1   |
| Lombok           | 1.18.32  |
| Apache Commons   | 3.12.0   |

---

## 🧱 Структура проекта

### `src/main/java`

**order**
- `OrderAPI` — методы работы с заказами
- `Order` — модель заказа
- `Ingredient` — модель ингредиента
- `OrderGenerator` — генерация случайных наборов ингредиентов

**user**
- `UserAPI` — создание / вход / редактирование / удаление пользователя
- `User` — модель пользователя
- `UserCredentials` — тестовые данные + генерация некорректных данных
- `UserGenerator` — генератор случайных email/паролей/имен

**service**
- `Service` — общие RestAssured спецификации
- `AuthResponse`, `IngredientsResponse` — модели ответов API

---

### `src/test/java`

**order**
- `OrderCreateTest` — создание заказов
- `OrderViewTest` — просмотр заказов

**user**
- `UserCreateTest` — регистрация
- `UserLoginTest` — авторизация
- `UserEditTest` — изменение данных

---



## 📊 Allure отчет

1. Запустить тесты:

```bash
  mvn clean test
```

2. Сгенерировать и открыть отчет:

```bash
  allure serve target/allure-results
```

---

## 🔧 Требования

- JDK 11+
- Maven 3+
- Установленный **Allure CLI**

