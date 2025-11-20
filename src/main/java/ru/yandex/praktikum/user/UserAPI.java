package ru.yandex.praktikum.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.service.AuthResponse;

import static io.restassured.RestAssured.given;

public class UserAPI {

    private static final String REGISTER_PATH = "auth/register";
    private static final String LOGIN_PATH = "auth/login";
    private static final String USER_PATH = "auth/user";

    @Step("Создание пользователя")
    public Response create(User user) {
        return given()
                .body(user)
                .when()
                .post(REGISTER_PATH);
    }

    @Step("Создание пользователя и маппинг ответа в AuthResponse")
    public AuthResponse createAsAuthResponse(User user) {
        return create(user).as(AuthResponse.class);
    }

    @Step("Логин пользователя")
    public Response login(UserCredentials creds) {
        return given()
                .body(creds)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Логин пользователя и маппинг ответа в AuthResponse")
    public AuthResponse loginAsAuthResponse(UserCredentials creds) {
        return login(creds).as(AuthResponse.class);
    }

    @Step("Изменение данных пользователя с авторизацией")
    public Response editData(String accessToken, User newUser) {
        return given()
                .header("authorization", accessToken)
                .body(newUser)
                .when()
                .patch(USER_PATH);
    }

    @Step("Изменение данных пользователя без токена")
    public Response editDataWithoutToken(User newUser) {
        return given()
                .body(newUser)
                .when()
                .patch(USER_PATH);
    }

    @Step("Удаление пользователя")
    public Response delete(String accessToken, User user) {
        return given()
                .header("authorization", accessToken)
                .body(user)
                .when()
                .delete(USER_PATH);
    }
}