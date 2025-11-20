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

public class UserCreateTest {

    private AuthResponse regResponse;
    private final UserAPI userAPI = new UserAPI();

    @Before
    public void setUp() {
        Service.setupSpecification();
    }

    @After
    @Step("Завершение - удаляем созданного пользователя (если он был создан)")
    public void tearDown() {
        if (regResponse != null && regResponse.getAccessToken() != null) {
            userAPI.delete(regResponse.getAccessToken(), UserCredentials.user);
        }
    }

    @Test
    @DisplayName("Создать нового пользователя и проверить ответ")
    @Description("Позитивный сценарий: создаём нового пользователя и проверяем тело ответа")
    public void createNewUserAndCheckResponse() {
        regResponse = userAPI.createAsAuthResponse(UserCredentials.user);
        checkBodyOfResponse(regResponse);
    }

    @Step("Проверка ответов успешного создания пользователя")
    public void checkBodyOfResponse(AuthResponse response) {
        assertTrue(response.isSuccess());
        assertEquals(UserCredentials.fakeEmail, response.getUser().getEmail());
        assertEquals(UserCredentials.fakeName, response.getUser().getName());
        assertFalse(response.getAccessToken().isBlank());
        assertFalse(response.getRefreshToken().isBlank());
    }

    @Test
    @DisplayName("Создание дублирующегося пользователя и проверка ответа")
    @Description("Создаём пользователя дважды и проверяем, что второй запрос возвращает ошибку 403")
    public void createDoubleUserAndCheckResponse() {
        regResponse = userAPI.createAsAuthResponse(UserCredentials.user);
        checkBodyOfResponse(regResponse);

        Response newResponse = userAPI.create(UserCredentials.user);
        checkStatusCodeOfBadRequest(newResponse);
        checkBodyOfDoubleRequest(newResponse);
    }

    @Step("Проверка статуса ответа (403 FORBIDDEN)")
    public void checkStatusCodeOfBadRequest(Response response) {
        assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusCode());
    }

    @Step("Проверка тела ответа при создании дубликата пользователя")
    public void checkBodyOfDoubleRequest(Response response) {
        response.then()
                .body("success", is(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без почты")
    @Description("Проверяем ошибку при попытке создать пользователя без email")
    public void createUserWithoutEmailAndCheckResponse() {
        Response response = sendPostRequestUserWithoutEmail();
        checkStatusCodeOfBadRequest(response);
        checkMessageToRequestWithoutAnyField(response);
    }

    @Step("Отправка запроса на создание юзера без почты")
    public Response sendPostRequestUserWithoutEmail() {
        return userAPI.create(UserCredentials.userWithoutEmail);
    }

    @Step("Проверка сообщения при отсутствии обязательного поля")
    public void checkMessageToRequestWithoutAnyField(Response response) {
        response.then()
                .body("success", is(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверяем ошибку при попытке создать пользователя без пароля")
    public void createUserWithoutPasswordAndCheckResponse() {
        Response response = sendPostRequestUserWithoutPassword();
        checkStatusCodeOfBadRequest(response);
        checkMessageToRequestWithoutAnyField(response);
    }

    @Step("Отправка запроса на создание юзера без пароля")
    public Response sendPostRequestUserWithoutPassword() {
        return userAPI.create(UserCredentials.userWithoutPassword);
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверяем ошибку при попытке создать пользователя без имени")
    public void createUserWithoutNameAndCheckResponse() {
        Response response = sendPostRequestUserWithoutName();
        checkStatusCodeOfBadRequest(response);
        checkMessageToRequestWithoutAnyField(response);
    }

    @Step("Отправка запроса на создание юзера без имени")
    public Response sendPostRequestUserWithoutName() {
        return userAPI.create(UserCredentials.userWithoutName);
    }
}