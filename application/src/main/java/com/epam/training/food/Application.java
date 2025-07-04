package com.epam.training.food;

import com.epam.training.food.data.OrderWriter;
import com.epam.training.food.domain.Credentials;
import com.epam.training.food.domain.Customer;
import com.epam.training.food.service.LowBalanceException;
import com.epam.training.food.domain.Order;
import com.epam.training.food.service.AuthenticationException;
import com.epam.training.food.service.DefaultFoodDeliveryService;
import com.epam.training.food.values.FoodSelection;
import com.epam.training.food.view.CLIView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Scanner;

@Component
public class Application implements CommandLineRunner {
    private DefaultFoodDeliveryService foodDeliveryService;
    private final Scanner scanner = new Scanner(System.in);
    private final OrderWriter writer = new OrderWriter();
    CLIView cliView;
    Customer customer;

    @Override
    public void run(String... args) throws Exception {
        cliView = new CLIView();

        if (loginAndVerifyUser()) {
            shoppingState();

            writer.writeOrdersToFile(foodDeliveryService.getALlOrdersDTO(), Path.of("./orders"));
        } else {
            System.out.println("Authentication failed. Program terminating.");
        }
    }

    @Autowired
    private void setFoodDeliveryService(DefaultFoodDeliveryService foodDeliveryService) {
        this.foodDeliveryService = foodDeliveryService;
    }


    private boolean loginAndVerifyUser() {
        Credentials credentials = cliView.readCredentials();

        try {
            customer = foodDeliveryService.authenticate(credentials);

            cliView.printWelcomeMessage(customer);
            return true;
        } catch (AuthenticationException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    private void shoppingState() {
        boolean userWantsToContinueShopping = true;


        while (userWantsToContinueShopping) {
            cliView.printAllFoods(foodDeliveryService.listAllFood());

            FoodSelection foodSelection = cliView.readFoodSelection(foodDeliveryService.listAllFood());

            updateCart(foodSelection);

            userWantsToContinueShopping = doesUserWantToContinueShopping();
        }
    }

    private boolean doesUserWantToContinueShopping() {

        boolean validChoice = false;
        boolean userWantsToContinueShopping = true;

        while (!validChoice) {
            System.out.println("Do you wish to: ");
            System.out.println("1. Add more items");
            System.out.println("2. Checkout");
            System.out.print("Enter your choice (1 or 2): ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                validChoice = true;
            } else if (choice.equals("2")) {
                validChoice = true;
                userWantsToContinueShopping = false;
                try {
                    checkout();
                } catch (IllegalStateException e) {
                    System.out.println("Error while making Order: Cart is empty");
                    validChoice = false;
                }
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
        return userWantsToContinueShopping;
    }

    private void updateCart(FoodSelection foodSelection) {
        try {
            foodDeliveryService.updateCart(customer, foodSelection.food(), foodSelection.amount());
            cliView.printAddedToCart(foodSelection.food(), foodSelection.amount());
        } catch (LowBalanceException e) {
            System.out.println("Unable to add current order for" + foodSelection.food() +
                    ", as with current cart content it would exceed available balance!\n");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void checkout() {
        Order order = foodDeliveryService.createOrder(customer);
        cliView.printOrderCreatedStatement(order, customer.getBalance());
    }

}
