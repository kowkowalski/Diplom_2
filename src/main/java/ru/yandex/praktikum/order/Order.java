package ru.yandex.praktikum.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Модель заказа для создания / просмотра.
 * Используем Lombok, как советовал ревьюер.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private List<String> ingredients;
}