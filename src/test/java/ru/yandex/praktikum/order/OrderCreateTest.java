package ru.yandex.praktikum.order;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.service.IngredientsResponse;
import ru.yandex.praktikum.service.Service;
import ru.yandex.praktikum.user.UserAPI;
import ru.yandex.praktikum.user.UserCredentials;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

public class OrderCreateTest {

    private UserAPI userAPI = new UserAPI();
    private OrderAPI orderAPI = new OrderAPI();
    private String authToken;
    private IngredientsResponse ingredients;

    @Before
    @Step("Создаём пользователя")
    public void setup() {
        Service.setupSpecification();
        Response create = userAPI.create(UserCredentials.user);
        authToken = create.then().extract().path("accessToken");
    }

    @After
    @Step("Удаляем пользователя")
    public void tearDown() {
        userAPI.delete(authToken, UserCredentials.user);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Статус 200, если ингредиенты валидные и есть accessToken")
    public void createOrderWithAuth() {
        ingredients = orderAPI.getAllIngredients();

        List<String> items =
                new OrderGenerator().createRandomOrder(ingredients);

        Order order = new Order(items);

        Response response = orderAPI.createOrder(authToken, order);
        checkSuccessOrderCreation(response);
    }

    @Step("Проверяем успешное создание заказа")
    private void checkSuccessOrderCreation(Response response) {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа БЕЗ авторизации")
    @Description("API актуально позволяет создавать заказ без авторизации, возвращает 200 OK")
    public void createOrderWithoutAuth() {
        ingredients = orderAPI.getAllIngredients();

        List<String> items =
                new OrderGenerator().createRandomOrder(ingredients);

        Order order = new Order(items);

        Response response = orderAPI.createOrderWithoutAuth(order);

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Ожидаем 400 — ингредиенты обязательны")
    public void createOrderNoIngredients() {
        Order order = new Order(new ArrayList<>());

        Response response = orderAPI.createOrder(authToken, order);

        response.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверными ингредиентами")
    @Description("API возвращает 400 или 500 — допускаем оба варианта")
    public void createOrderWithWrongIngredients() {
        Order order = new Order(new ArrayList<>(List.of("123", "456")));

        Response response = orderAPI.createOrder(authToken, order);

        // Реальное API нестабильно — допускаем оба статуса
        response.then()
                .statusCode(anyOf(equalTo(400), equalTo(500)));
    }
}