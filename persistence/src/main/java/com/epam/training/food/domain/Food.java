package com.epam.training.food.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private BigDecimal calorie;
    private String description;
    private BigDecimal price;

    public Food() {
    }

    public Food(Long id ,String name, BigDecimal calorie, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.calorie = calorie;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCalorie() {
        return calorie;
    }

    public void setCalorie(BigDecimal calorie) {
        this.calorie = calorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Food food = (Food) o;
        return Objects.equals(name, food.name) && Objects.equals(calorie, food.calorie) && Objects.equals(description,
            food.description) && Objects.equals(price, food.price);
    }

    @Override public int hashCode() {
        return Objects.hash(name, calorie, description, price);
    }

    @Override public String toString() {
        return "Food{" +
            "name='" + name + '\'' +
            ", calorie=" + calorie +
            ", description='" + description + '\'' +
            ", price=" + price +
            '}';
    }

    public static class Builder {
        private String name;
        private BigDecimal calorie;
        private String description;
        private BigDecimal price;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder calorie(BigDecimal calorie) {
            this.calorie = calorie;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

//        public Food build() {
//            return new Food( id, name, calorie, description, price);
//        }
    }
}
