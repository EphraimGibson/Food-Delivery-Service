package com.epam.training.food.data;

import com.epam.training.food.domain.Customer;
import com.epam.training.food.domain.Food;
import com.epam.training.food.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FileDataStore implements DataStore {

    private final String inputFolderPath;

    private List<Customer> customerList;
    private  List<Food> foodList;
    private final List<Order> orderList = new ArrayList<>();
    private OrderWriter orderWriter;

    public FileDataStore(@Value("${baseDirPath}") String inputFolderPath) {
        this.inputFolderPath = inputFolderPath;
    }

    @Autowired
    public void setOrderWriter(OrderWriter orderWriter) {
        this.orderWriter = orderWriter;
    }

    @PostConstruct
    public void init() {
        Path customerFile = Paths.get(inputFolderPath, "customers.csv");
        if (Files.exists(customerFile)){
            customerList = new CustomerReader().read(customerFile.toString());
        }

        Path foodFile = Paths.get(inputFolderPath, "foods.csv");
        if (Files.exists(foodFile)){
            foodList = new FoodReader().read(foodFile.toString());
        }
    }

    @Override
    public List<Customer> getCustomers() {
        return customerList;
    }

    @Override
    public List<Food> getFoods() {
        return foodList;
    }

    @Override
    public List<Order> getOrders() {
        return orderList;
    }

    @Override
    public Order createOrder(Order order) {
        orderList.add(order);
        return order;
    }


    @Override
    public void writeOrders() {
        Path ordersFilePath = Paths.get(inputFolderPath, "orders.csv");
        orderWriter.writeOrdersToFile(orderList, ordersFilePath);
    }
}
