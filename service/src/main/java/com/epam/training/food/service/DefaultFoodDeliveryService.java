package com.epam.training.food.service;

import com.epam.training.food.aspect.EnableArgumentLogging;
import com.epam.training.food.aspect.EnableExecutionTimeLogging;
import com.epam.training.food.aspect.EnableReturnValueLogging;
import com.epam.training.food.domain.*;
import com.epam.training.food.repository.CustomerRepository;
import com.epam.training.food.repository.FoodRepository;
import com.epam.training.food.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DefaultFoodDeliveryService implements FoodDeliveryService {
    private final ShoppingAssistant shoppingAssistant = new ShoppingAssistant();

    private CustomerRepository customerRepository;

    private FoodRepository foodRepository;

    private OrderRepository orderRepository;


    public DefaultFoodDeliveryService(CustomerRepository customerRepository, FoodRepository foodRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.foodRepository = foodRepository;
        this.orderRepository = orderRepository;
    }

    @EnableArgumentLogging
    @EnableReturnValueLogging
    @EnableExecutionTimeLogging
    @Override
    public Customer authenticate(Credentials credentials) throws AuthenticationException {
        String username = credentials.getUserName();
        String password = credentials.getPassword();

       return customerRepository.findCustomerByUserNameAndPassword(username, password)
               .orElseThrow(() -> new AuthenticationException("invalid Credentials"));
    }

    @EnableReturnValueLogging
    @Override
    public List<Food> listAllFood() {
        return foodRepository.findAll();
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
        orderRepository.save(newOrder);
        return newOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}

