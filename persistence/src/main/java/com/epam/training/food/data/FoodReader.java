package com.epam.training.food.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.epam.training.food.domain.Food;

public class FoodReader extends Reader {

    @Override public List<Food> read(String pathOfFile) {
        var lines = readFromFile(pathOfFile);
        var foods = new ArrayList<Food>(lines.size());
        for (var s : lines) {
            foods.add(buildFood(s));
        }
        return foods;
    }

    private Food buildFood(String line) {
        var strings = splitLineByComma(line);
        return new Food.Builder()
            .name(strings[0])
            .calorie(new BigDecimal(strings[1]))
            .description(strings[2])
            .price(new BigDecimal(strings[3]))
            .build();
    }
}
