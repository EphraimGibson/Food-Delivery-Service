package com.epam.training.food.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "_Order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    //the field price can be used later to add discounts, so it would differ from the sum of food prices
    private BigDecimal price;

    private LocalDateTime timestampCreated;

    public Order() {
    }

    public Order(Customer customer) {
        this.customer = customer;
        this.orderItems = customer.getCart().getOrderItems();
        this.price = customer.getCart().getPrice();
        this.timestampCreated = LocalDateTime.now();
        this.orderId = null;
    }

    public Order(Long orderId, Customer customer, List<OrderItem> orderItems, BigDecimal price, LocalDateTime timestampCreated) {
        this.orderId = orderId;
        this.customer = customer;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        return customer == order.customer && Objects.equals(orderId, order.orderId) && Objects.equals(orderItems, order.orderItems)
            && Objects.equals(price, order.price) && Objects.equals(timestampCreated, order.timestampCreated);
    }

    @Override public int hashCode() {
        return Objects.hash(orderId, customer, orderItems, price, timestampCreated);
    }

    @Override public String toString() {
        return "Order{" +
            "orderId=" + orderId +
            ", customer=" + customer +
            ", items=" + orderItems +
            ", price=" + price +
            ", timestampCreated=" + timestampCreated +
            '}';
    }

}
