package com.epam.training.food.data;

import java.util.List;

import com.epam.training.food.domain.Customer;
import com.epam.training.food.domain.Food;
import com.epam.training.food.domain.Order;

public interface DataStore {
    List<Customer> getCustomers();

    List<Food> getFoods();

    List<Order> getOrders();

    Order createOrder(Order order);

    void writeOrders();
}
