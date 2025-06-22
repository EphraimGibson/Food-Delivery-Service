package com.epam.training.food.view;

import com.epam.training.food.domain.Credentials;
import com.epam.training.food.domain.Customer;
import com.epam.training.food.domain.Food;
import com.epam.training.food.domain.Order;
import com.epam.training.food.values.FoodSelection;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CLIView implements View{
    @Override
    public Credentials readCredentials() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter customer name:");
        String name = scanner.nextLine();

        System.out.println("Enter customer password:");
        String pass = scanner.nextLine();

        return new Credentials(name,pass);
    }

    @Override
    public void printWelcomeMessage(Customer customer) {
        System.out.println("Welcome, "+ customer.getName()+ ". Your balance is: "+ customer.getBalance()+" EUR.");
    }

    @Override
    public void printAllFoods(List<Food> foods) {
        System.out.println("Foods offered today:");

        for (Food food : foods){
            System.out.println("- "+ food.getName()+" "+ food.getPrice() + "EUR each");
        }
    }

    @Override
    public FoodSelection readFoodSelection(List<Food> foods) {
        System.out.print("Please enter the name and amount of food (separated by comma) you would like to buy:");
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();
        String[] parts = input.split(",");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Input must contain a food name and amount separated by comma");
        }

        String foodName = parts[0].trim();
        int amount;

        try {
            amount = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be a valid number");
        }


        //find food in list of foods
        Food food = foods.stream()
                .filter(f->f.getName().equals(foodName))
                .findFirst()
                .orElseThrow();

        return new FoodSelection(food,amount);
    }

    @Override
    public void printAddedToCart(Food food, int pieces) {
        System.out.println("Added "+ pieces + "piece(s) of " + food.getName() +" to the cart." );
    }

    @Override
    public void printErrorMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void printOrderCreatedStatement(Order order, BigDecimal balance) {

        String[] foods = order.getOrderItems()
                .stream()
                .map(item->item.getFood().getName())
                .toArray(String[]::new);

        System.out.println("items: "+ Arrays.toString(foods)+"," +
                " price: "+order.getPrice()+ " EUR," +
                " timestamp:"+ order.getTimestampCreated()+ " has been confirmed." );

        System.out.println("Your balance is "+ balance+ " EUR.");
        System.out.println("Thank you for your purchase.");
    }
}
