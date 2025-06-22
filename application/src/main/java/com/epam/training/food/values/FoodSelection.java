package com.epam.training.food.values;

import com.epam.training.food.domain.Food;


public record FoodSelection(Food food, Integer amount) {
    public static final FoodSelection NONE = new FoodSelection(new Food(), -1);
}
