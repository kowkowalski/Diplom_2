package ru.yandex.praktikum.service;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * Общая настройка RestAssured для всех тестов.
 */
public class Service {

    // Базовый URL из задания 2
    public static final String BASE_URL = "https://stellarburgers.education-services.ru/";
    public static final String BASE_PATH = "api";

    /**
     * Метод, который вызывается в @Before всех тестов.
     * Его как раз и ждут твои тестовые классы (setupSpecification()).
     */
    public static void setupSpecification() {
        RestAssured.requestSpecification = getRequestSpecification();
        RestAssured.responseSpecification = getResponseSpecification();
    }

    private static RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setBasePath(BASE_PATH)
                .setContentType(ContentType.JSON)
                .build();
    }

    private static ResponseSpecification getResponseSpecification() {
        return new ResponseSpecBuilder()
                .build();
    }
}