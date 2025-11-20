package ru.yandex.praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.service.Service;
import ru.yandex.praktikum.user.UserAPI;
import ru.yandex.praktikum.user.UserCredentials;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderViewTest {

    private final UserAPI userAPI = new UserAPI();
    private final OrderAPI orderAPI = new OrderAPI();
    private String authToken;

    @Before
    @Step("Начало - создаём пользователя и настраиваем спецификации")
    public void setUp() {
        Service.setupSpecification();
        Response loginResponse = userAPI.create(UserCredentials.user);
        authToken = loginResponse.then().extract().path("accessToken");
    }

    @After
    @Step("Завершение - удаляем созданного пользователя")
    public void tearDown() {
        if (authToken != null) {
            userAPI.delete(authToken, UserCredentials.user);
        }
    }

    @Test
    @DisplayName("Получение списка заказов пользователя")
    @Description("Проверяем, что авторизованный пользователь может получить список своих заказов")
    public void getOrdersByUser() {
        Response response = orderAPI.getOrdersByCurrentUser(authToken);
        checkResponseWhenGetListOfOrders(response);
    }

    @Step("Проверка ответа получения списка заказов (авторизованный пользователь)")
    public void checkResponseWhenGetListOfOrders(Response response) {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .and()
                .body("orders", notNullValue())
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    @Description("Проверяем, что без авторизации список заказов получить нельзя")
    public void getUserOrderWithoutAuthorization() {
        Response response = orderAPI.getOrdersWithoutAuth();
        checkResponseWhenGetListOfOrdersWithoutAuthorization(response);
    }

    @Step("Проверка ответа получения списка заказов (без авторизации)")
    public void checkResponseWhenGetListOfOrdersWithoutAuthorization(Response response) {
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}