package com.epam.training.food.service;

import com.epam.training.food.aspect.EnableArgumentLogging;
import com.epam.training.food.aspect.EnableExecutionTimeLogging;
import com.epam.training.food.aspect.EnableReturnValueLogging;
import com.epam.training.food.domain.*;
import com.epam.training.food.repository.CustomerRepository;
import com.epam.training.food.repository.FoodRepository;
import com.epam.training.food.repository.OrderRepository;
import com.epam.training.food.utility.ShoppingStateUtility;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultFoodDeliveryService implements FoodDeliveryService {
    private final ShoppingStateUtility shoppingAssistant;
    private final CustomerRepository customerRepository;
    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;


    public DefaultFoodDeliveryService(CustomerRepository customerRepository, FoodRepository foodRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.foodRepository = foodRepository;
        this.orderRepository = orderRepository;
        shoppingAssistant = new ShoppingStateUtility();
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
    @Transactional
    public void updateCart(Customer customer, Food food, int pieces) {
        if (pieces < 0) throw new IllegalArgumentException("Number of items can not be negative");

        BigDecimal itemsTotal = food.getPrice().multiply(BigDecimal.valueOf(pieces));
        OrderItem item = new OrderItem(food, pieces, itemsTotal);

        shoppingAssistant.updateItem(customer, item);
        shoppingAssistant.updateTotalPriceOfOrderItemsInTheCart(customer);
    }

    @Override
    @Transactional
    public Order createOrder(Customer customer) throws IllegalStateException {
        Cart cart = customer.getCart();

        Customer managedCustomer = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new EntityNotFoundException("Unable to find customer to place order"));

        checkForEmptyCart(cart);
        checkSufficientBalance(managedCustomer, cart);

        return managedCustomer.makeOrder();
    }

    private static void checkSufficientBalance(Customer customer, Cart cart) {
        if (customer.getBalance().compareTo(cart.getPrice()) >= 0) {
            customer.setCart(cart);
        } else {
            throw new LowBalanceException("Balance to0 low to place order");
        }
    }

    private static void checkForEmptyCart(Cart cart) {
        if (cart.getOrderItems().isEmpty()) throw new IllegalStateException("Unable to place order! Cart is empty");
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public List<OrderDTO> getALlOrdersDTO() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    List<OrderItemDTO> orderItemDTOList = order.getOrderItems().stream()
                            .map(item -> new OrderItemDTO(item.getFood().getId(), item.getFood().getName(), item.getPieces(), item.getPrice()))
                            .collect(Collectors.toList());
                    return new OrderDTO(order.getOrderId(), order.getCustomer().getId(), orderItemDTOList, order.getPrice(), order.getTimestampCreated());
                }).collect(Collectors.toList());
    }
}
