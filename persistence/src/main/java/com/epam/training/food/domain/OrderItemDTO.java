package com.epam.training.food.domain;

import java.math.BigDecimal;

public record OrderItemDTO (Long foodId,String foodName, int pieces, BigDecimal price){
}
