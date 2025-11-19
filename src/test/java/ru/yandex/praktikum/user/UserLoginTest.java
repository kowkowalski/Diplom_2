package ru.yandex.praktikum.user;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.service.AuthResponse;
import ru.yandex.praktikum.service.Service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class UserLoginTest {

    private final UserAPI userAPI = new UserAPI();
    private AuthResponse loginResponse;
    private String accessTokenFromCreate;

    @Before
    @Step("Начало - создаём пользователя для тестов логина")
    public void setUp() {
        Service.setupSpecification();
        Response createResp = userAPI.create(UserCredentials.user);
        accessTokenFromCreate = createResp.then().extract().path("accessToken");
    }

    @After
    @Step("Завершение - удаляем созданного пользователя")
    public void tearDown() {
        if (accessTokenFromCreate != null) {
            userAPI.delete(accessTokenFromCreate, UserCredentials.user);
        }
    }

    @Test
    @DisplayName("Проверка успешного логина")
    @Description("Позитивный сценарий логина: правильный email и пароль")
    public void checkLoginUser() {
        loginResponse = userAPI.loginAsAuthResponse(UserCredentials.from(UserCredentials.user));
        checkBodyOfResponse(loginResponse);
    }

    @Step("Проверка ответов успешного логина")
    public void checkBodyOfResponse(AuthResponse response) {
        assertTrue(response.isSuccess());
        assertFalse(response.getAccessToken().isBlank());
        assertFalse(response.getRefreshToken().isBlank());
        assertEquals(UserCredentials.fakeEmail, response.getUser().getEmail());
        assertEquals(UserCredentials.fakeName, response.getUser().getName());
    }

    @Test
    @DisplayName("Логин с неверным логином")
    @Description("Проверяем, что при неверном email логин не проходит")
    public void loginWithIncorrectEmailAndCheckResponse() {
        Response response = loginIncorrectUserWithWrongEmail();
        checkStatusCodeWithIncorrectData(response);
        checkMessageWithIncorrectData(response);
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверяем, что при неверном пароле логин не проходит")
    public void loginWithIncorrectPasswordAndCheckResponse() {
        Response response = loginIncorrectUserWithWrongPassword();
        checkStatusCodeWithIncorrectData(response);
        checkMessageWithIncorrectData(response);
    }

    @Step("Авторизация юзера с неверным email")
    public Response loginIncorrectUserWithWrongEmail() {
        return userAPI.login(UserCredentials.withWrongEmail());
    }

    @Step("Авторизация юзера с неверным паролем")
    public Response loginIncorrectUserWithWrongPassword() {
        return userAPI.login(UserCredentials.withWrongPassword());
    }

    @Step("Проверка статуса ответа при неправильных данных")
    public void checkStatusCodeWithIncorrectData(Response response) {
        response.then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Step("Проверка тела ответа при неправильных данных")
    public void checkMessageWithIncorrectData(Response response) {
        response.then()
                .body("success", is(false))
                .and()
                .assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }
}