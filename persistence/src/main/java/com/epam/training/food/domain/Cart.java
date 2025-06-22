package com.epam.training.food.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart {
    private List<OrderItem> orderItems;
    //the field price can be used later to add discounts, so it would differ from the sum of the OrderItems
    private BigDecimal price;

    public Cart() {
    }

    private Cart(List<OrderItem> orderItems, BigDecimal price) {
        this.orderItems = orderItems;
        this.price = price;
    }

    //this utility method is not part of the homework specification!
    public static Cart getEmptyCart() {
        return new Cart(new ArrayList<>(), BigDecimal.ZERO);
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Cart cart = (Cart) o;
        return Objects.equals(orderItems, cart.orderItems) && Objects.equals(price, cart.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItems, price);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "orderItems=" + orderItems +
                ", price=" + price +
                '}';
    }
}
