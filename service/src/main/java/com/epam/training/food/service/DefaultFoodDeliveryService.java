package com.epam.training.food.service;

import com.epam.training.food.aspect.EnableArgumentLogging;
import com.epam.training.food.aspect.EnableExecutionTimeLogging;
import com.epam.training.food.aspect.EnableReturnValueLogging;
import com.epam.training.food.data.FileDataStore;
import com.epam.training.food.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DefaultFoodDeliveryService implements FoodDeliveryService {
    private final FileDataStore data;
    private final ShoppingAssistant shoppingAssistant = new ShoppingAssistant();

    @Autowired
    public DefaultFoodDeliveryService(FileDataStore fileDataStore) {
        this.data = fileDataStore;
    }

    @EnableArgumentLogging
    @EnableReturnValueLogging
    @EnableExecutionTimeLogging
    @Override
    public Customer authenticate(Credentials credentials) throws AuthenticationException {
        return data.getCustomers()
                .stream()
                .filter(customer -> credentials.getUserName().equals(customer.getUserName())
                        && credentials.getPassword().equals(customer.getPassword()))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException("invalid Credentials"));
    }

    @EnableReturnValueLogging
    @Override
    public List<Food> listAllFood() {
        return data.getFoods();
    }

    @EnableArgumentLogging
    @EnableExecutionTimeLogging
    @Override
    public void updateCart(Customer customer, Food food, int pieces) {
        if (pieces < 0) throw new IllegalArgumentException("Number of items can not be negative");

        BigDecimal itemsTotal = food.getPrice().multiply(BigDecimal.valueOf(pieces));
        OrderItem item = new OrderItem(food, pieces, itemsTotal);

        shoppingAssistant.updateItem(customer ,item);
        shoppingAssistant.updateTotalPriceOfOrderItemsInTheCart(customer);
    }

    @Override
    public Order createOrder(Customer customer) throws IllegalStateException {
        Order newOrder = customer.makeOrder();
        data.createOrder(newOrder);
        return newOrder;
    }


}

