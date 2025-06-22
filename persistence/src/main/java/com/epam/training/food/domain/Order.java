package com.epam.training.food.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long orderId;
    private long customerId;
    private List<OrderItem> orderItems;
    //the field price can be used later to add discounts, so it would differ from the sum of food prices
    private BigDecimal price;
    private LocalDateTime timestampCreated;

    public Order() {
    }

    public Order(Customer customer) {
        this.customerId = customer.getId();
        this.orderItems = customer.getCart().getOrderItems();
        this.price = customer.getCart().getPrice();
        this.timestampCreated = LocalDateTime.now();
        this.orderId = null;
    }

    public Order(Long orderId, long customerId, List<OrderItem> orderItems, BigDecimal price, LocalDateTime timestampCreated) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderItems = orderItems;
        this.price = price;
        this.timestampCreated = timestampCreated;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
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

    public LocalDateTime getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(LocalDateTime timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Order order = (Order) o;
        return customerId == order.customerId && Objects.equals(orderId, order.orderId) && Objects.equals(orderItems, order.orderItems)
            && Objects.equals(price, order.price) && Objects.equals(timestampCreated, order.timestampCreated);
    }

    @Override public int hashCode() {
        return Objects.hash(orderId, customerId, orderItems, price, timestampCreated);
    }

    @Override public String toString() {
        return "Order{" +
            "orderId=" + orderId +
            ", customerId=" + customerId +
            ", items=" + orderItems +
            ", price=" + price +
            ", timestampCreated=" + timestampCreated +
            '}';
    }

}
