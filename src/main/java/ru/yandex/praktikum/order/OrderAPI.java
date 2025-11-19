package ru.yandex.praktikum.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import ru.yandex.praktikum.service.IngredientsResponse;

import static io.restassured.RestAssured.given;

public class OrderAPI {

    private static final String ORDER_PATH = "orders";
    private static final String INGREDIENTS_PATH = "ingredients";

    @Step("Создание заказа (авторизованный пользователь)")
    public Response createOrder(String accessToken, Order order) {
        return given()
                .header("authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Создание заказа (без авторизации)")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Получение заказов текущего пользователя")
    public Response getOrdersByCurrentUser(String accessToken) {
        return given()
                .header("authorization", accessToken)
                .when()
                .get(ORDER_PATH);
    }

    @Step("Получение заказов без авторизации")
    public Response getOrdersWithoutAuth() {
        return given()
                .when()
                .get(ORDER_PATH);
    }

    @Step("Получение списка всех возможных ингредиентов")
    public IngredientsResponse getAllIngredients() {
        return given()
                .when()
                .get(INGREDIENTS_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .as(IngredientsResponse.class);
    }
}