package ru.yandex.praktikum.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.praktikum.order.Ingredient;

import java.util.List;

/**
 * Обёртка для ответа /ingredients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientsResponse {

    private boolean success;
    private List<Ingredient> data;
}