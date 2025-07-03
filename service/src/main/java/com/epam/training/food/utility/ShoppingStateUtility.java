package com.epam.training.food.utility;

import com.epam.training.food.domain.Cart;
import com.epam.training.food.domain.Customer;
import com.epam.training.food.domain.OrderItem;
import com.epam.training.food.service.LowBalanceException;

import java.math.BigDecimal;
import java.util.stream.IntStream;

public class ShoppingStateUtility {
    public static final int NOT_FOUND_IN_CART = -1;

    public ShoppingStateUtility() {
    }

    public void updateItem(Customer customer, OrderItem item) {

        if (customer.getCart() == null) customer.setCart(Cart.getEmptyCart());

        if (customer.getCart().getOrderItems().isEmpty() && item.getPieces() != 0) {
            addItemAndAdjustBalance(customer, item);
            return;
        }
        int itemIndexInCart = findItemInCart(customer, item);

        if (itemIndexInCart == NOT_FOUND_IN_CART) {
            processAddNewItem(customer, item);
        } else {
            processCartItemUpdate(customer, itemIndexInCart, item);
        }
    }

    private void checkIfBalanceIsSufficient(Customer customer, BigDecimal amount) {
        BigDecimal totalPriceOfOrderItems = getPriceOfOrderItems(customer);
        BigDecimal remainingBalance = customer.getBalance().subtract(totalPriceOfOrderItems);

        if (remainingBalance.compareTo(amount) < 0) {
            throw new LowBalanceException("Insufficient balance to add item");
        }
    }

    private int findItemInCart(Customer customer, OrderItem item) {
        return IntStream.range(0, customer.getCart().getOrderItems().size())
                .filter(i -> customer.getCart().getOrderItems().get(i).getFood().getName()
                        .equals(item.getFood().getName()))
                .findFirst()
                .orElse(NOT_FOUND_IN_CART);
    }

    private void addItemAndAdjustBalance(Customer customer, OrderItem item) {
        this.checkIfBalanceIsSufficient(customer, item.getPrice());
        customer.getCart().getOrderItems().add(item);
    }

    private void removeItemAndRefundBalance(Customer customer, int index) {
        customer.getCart().getOrderItems().remove(index);
    }

    private void updateItemAndAdjustBalance(Customer customer, int index, OrderItem item) {
        this.checkIfBalanceIsSufficient(customer, item.getPrice());
        customer.getCart().getOrderItems().set(index, item);
    }

    private void processAddNewItem(Customer customer, OrderItem item) {
        if (item.getPieces() == 0) {
            throw new IllegalArgumentException("Item does not exist in Cart");
        } else {
            addItemAndAdjustBalance(customer, item);
        }
    }

    private void processCartItemUpdate(Customer customer, int itemIndex, OrderItem item) {
        if (item.getPieces() > 0) {
            updateItemAndAdjustBalance(customer, itemIndex, item);
        } else {
            removeItemAndRefundBalance(customer, itemIndex);
        }
    }

    public void updateTotalPriceOfOrderItemsInTheCart(Customer customer) {
        BigDecimal priceOfOrderItems = getPriceOfOrderItems(customer);
        customer.getCart().setPrice(priceOfOrderItems);
    }

    private BigDecimal getPriceOfOrderItems(Customer customer) {
        return customer.getCart().getOrderItems().stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
