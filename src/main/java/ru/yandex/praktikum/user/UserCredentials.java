package ru.yandex.praktikum.user;

public class UserCredentials {

    // Основные тестовые данные
    public static final String fakeEmail = "test.api.user@example.com";
    public static final String fakeName = "Test Api User";
    public static final String fakePassword = "Password123";

    public static final String newFakeEmail = "new.test.api.user@example.com";
    public static final String newFakeName = "New Test Api User";
    public static final String newFakePassword = "NewPassword123";

    // Данные для негативных тестов логина
    public static final String wrongEmail = "wrong_" + fakeEmail;
    public static final String wrongPassword = "WrongPassword123";

    // Готовые пользователи (используются в тестах)
    public static final User user =
            new User(fakeEmail, fakePassword, fakeName);

    public static final User newUser =
            new User(newFakeEmail, newFakePassword, newFakeName);

    public static final User userWithoutEmail =
            new User(null, fakePassword, fakeName);

    public static final User userWithoutPassword =
            new User(fakeEmail, null, fakeName);

    public static final User userWithoutName =
            new User(fakeEmail, fakePassword, null);

    // Объект, который API принимает при логине
    private String email;
    private String password;

    public UserCredentials() {}

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCredentials from(User user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }

    public static UserCredentials withWrongEmail() {
        return new UserCredentials(wrongEmail, fakePassword);
    }

    public static UserCredentials withWrongPassword() {
        return new UserCredentials(fakeEmail, wrongPassword);
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }
}