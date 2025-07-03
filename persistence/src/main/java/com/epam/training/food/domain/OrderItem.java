package com.epam.training.food.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_Item_Id")
    private Long id;

    @OneToOne
    private Food food;

    private int pieces;
    //the field price can be used later to add discounts, so it would differ from (food x pieces)
    private BigDecimal price;

    public OrderItem() {
    }

    public OrderItem(Food food, int pieces, BigDecimal price) {
        this.food = food;
        this.pieces = pieces;
        this.price = price;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
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
        OrderItem orderItem = (OrderItem) o;
        return pieces == orderItem.pieces && Objects.equals(food, orderItem.food) && Objects.equals(price, orderItem.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(food, pieces, price);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "food=" + food +
                ", pieces=" + pieces +
                ", price=" + price +
                '}';
    }

    public static class Builder {
        private Food food;
        private int pieces;
        private BigDecimal price;

        public Builder food(Food food) {
            this.food = food;
            return this;
        }

        public Builder pieces(int pieces) {
            this.pieces = pieces;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(food, pieces, price);
        }
    }
}
