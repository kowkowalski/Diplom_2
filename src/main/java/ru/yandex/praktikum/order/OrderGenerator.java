package ru.yandex.praktikum.order;

import ru.yandex.praktikum.service.IngredientsResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderGenerator {

    private Random random = new Random();

    public List<String> createRandomOrder(IngredientsResponse ingredients) {

        List<String> result = new ArrayList<>();

        int total = ingredients.getData().size(); // <-- реальное количество ингров

        // минимум 1 ингредиент, максимум total
        int count = 1 + random.nextInt(total);

        for (int i = 0; i < count; i++) {
            int index = random.nextInt(total);
            result.add(ingredients.getData().get(index).getId());
        }

        return result;
    }
}