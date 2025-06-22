/*
 * Copyright © 2021 EPAM Systems, Inc. All Rights Reserved. All information contained herein is, and remains the
 * property of EPAM Systems, Inc. and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from EPAM Systems, Inc
 */

import com.epam.training.food.data.FileDataStore;
import  com.epam.training.food.service.*;

import com.epam.training.food.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DefaultFoodDeliveryServiceTest {
    private static final String INPUT_FOLDER_PATH = "../test";
    private static final long NEXT_ORDER_ID = 0L;

    private FileDataStore fileDataStore;

    private DefaultFoodDeliveryService underTest;

    @BeforeEach
    public void setup() {
        fileDataStore = new FileDataStore(INPUT_FOLDER_PATH);
        fileDataStore.init();
        assertAll("Some of the collections in FileDataStore are not initialized correctly.",
                () -> assertFalse(fileDataStore.getFoods().isEmpty(),
                        "Foods collection in FileDataStore is empty after FileDataStore.init() call"),
                () -> assertFalse(fileDataStore.getCustomers().isEmpty(),
                        "Customers collection in FileDataStore is empty after FileDataStore.init() call")
        );

        underTest = new DefaultFoodDeliveryService(fileDataStore);
    }

    @Test
    @DisplayName("Authenticate should return the correct customer when given valid credentials")
    public void testAuthenticateShouldReturnTheCorrectCustomerWhenGivenCorrectCredentials() {
        // GIVEN
        Credentials quickLoginCredentials = createQuickLoginCredentials();
        long expectedId = 1;

        // WHEN
        Customer customer = underTest.authenticate(quickLoginCredentials);

        // THEN
        assertEquals(expectedId, customer.getId(), "Wrong customer customer returned, IDs do not match.");
    }

    @Test
    @DisplayName("Authenticate should throw AuthenticationException when called with invalid credentials")
    public void testAuthenticateShouldThrowAuthenticationExceptionWhenGivenIncorrectCredentials() {
        // GIVEN
        Credentials credentials = new Credentials();
        credentials.setUserName("a");
        credentials.setPassword("incorrect");

        // WHEN

        // THEN
        assertThrows(AuthenticationException.class, () -> underTest.authenticate(credentials),
                "authenticate did not throw AuthenticationException when called with"
                        + " name: 'a' and password: 'incorrect'.");
    }

    @Test
    @DisplayName("createOrder should create an order with correct values"
            + " and add it to the customer and the datastore")
    public void testCreateOrderShouldCreateOrderWithCorrectValuesAndAddItToCustomerAndDataStore() {
        // GIVEN
        Customer customer = createCustomerWithCart(createOneItemCartWithOneFideua());

        Order expected = createOrder(customer, NEXT_ORDER_ID);

        // WHEN
        Order actual = underTest.createOrder(customer);

        // THEN
        assertThat(expected)
                .usingRecursiveComparison()
                .ignoringFields("timestampCreated")
                .withFailMessage("The created order has incorrect values!"
                        + " Expected: " + expected + ", actual: " + actual + "."
                        + " (timestampCreated field is ignored.)")
                .isEqualTo(actual);

        assertAll(
                () -> assertNewOrderAddedToCustomer(customer),
                this::assertNewOrderAddedToDataStore
        );
    }

    private void assertNewOrderAddedToCustomer(Customer customer) {
        int lastOrderIndexInCustomer = customer.getOrders().size() - 1;
        long idOfLastOrderInCustomer = customer.getOrders().get(lastOrderIndexInCustomer).getOrderId();
        assertEquals(NEXT_ORDER_ID, idOfLastOrderInCustomer,
                "The created order wasn't added to the Customer's order collection."
                        + " The ID of the last Order is incorrect.");
    }

    private void assertNewOrderAddedToDataStore() {
        int lastOrderIndexInDataStore = fileDataStore.getOrders().size() - 1;
        long idOfLastOrderInDataStore = fileDataStore.getOrders().get(lastOrderIndexInDataStore).getOrderId();
        assertEquals(NEXT_ORDER_ID, idOfLastOrderInDataStore,
                "The created order wasn't added to the DataStore. The ID of the last Order is incorrect.");
    }

    @Test
    @DisplayName("createOrder should empty the cart if the order creation was successful")
    public void testCreateOrderShouldEmptyTheCartIfOrderCreationWasSuccessful() {
        // GIVEN
        Customer customer = createCustomerWithCart(createOneItemCartWithOneFideua());

        // WHEN
        underTest.createOrder(customer);

        // THEN
        assertEquals(0, customer.getCart().getOrderItems().size(),
                "The shopping cart still contains items.");
    }

    @Test
    @DisplayName("createOrder should subtract the price of the order from the customer's balance"
            + " if the creation was successful")
    public void testCreateOrderShouldDeductFromTheCustomersBalanceIfOrderCreationWasSuccessful() {
        // GIVEN
        Customer customer = createCustomerWithCart(createOneItemCartWithOneFideua());

        // WHEN
        underTest.createOrder(customer);

        // THEN
        assertEquals(new BigDecimal(85), customer.getBalance(),
                "Wrong balance after order creation. Balance before: 100, price of order: 15.");
    }

    @Test
    @DisplayName("createOrder should throw IllegalStateException when the cart is empty")
    public void testCreateOrderShouldThrowIllegalStateExceptionWhenTheCartIsEmpty() {
        // GIVEN
        Customer customer = createCustomerWithCart(createEmptyCart());

        // WHEN

        // THEN
        Assertions.assertThrows(IllegalStateException.class, () -> underTest.createOrder(customer),
                "createOrder did not throw IllegalStateException when called with"
                        + " cart: " + customer.getCart());
    }

    @Test
    @DisplayName("updateCart should add a new item when the Food is not in the cart and pieces is positive")
    public void testUpdateCartShouldAddANewItemWhenFoodIsNotInCartAndPiecesIsPositive() {
        // GIVEN
        Customer customer = createCustomerWithCart(createEmptyCart());
        Cart expectedCart = createOneItemCartWithOneFideua();

        // WHEN
        underTest.updateCart(customer, createFideua(), 1);

        // THEN
        Cart actualCart = customer.getCart();
        assertAll("The cart has one or more incorrect values after updateCart was called with"
                        + " a customer with cart: " + customer.getCart() + " and food: Fideua!",
                () -> assertEquals(expectedCart.getOrderItems(), actualCart.getOrderItems(),
                        "The content of the cart if incorrect!"),
                () -> assertEquals(expectedCart.getPrice(), actualCart.getPrice(),
                        "The price of the cart is incorrect!")
        );
    }

    @Test
    @DisplayName("updateCart should not add a new item when the price of the item " +
            "and the current cart price would exceed player balance")
    public void testUpdateCartShouldNotAddNewItemWhenItemAndCartPriceWouldExceedUserBalance() {
        //GIVEN
        Customer customer = createCustomerWithCart(createEmptyCart());

        //WHEN
        //THEN
        Assertions.assertThrows(LowBalanceException.class,
                () -> underTest.updateCart(customer, createFideua(), 100),
                String.format("Expected LowBalance exception to be thrown, as 100 pieces of Fideua has been" +
                        "added to the cart. Initial customer was: %s", createCustomerWithCart(createEmptyCart()))
        );
    }

    @Test
    @DisplayName("updateCart should update the item when the Food is already in the cart"
            + " and pieces is positive")
    public void testUpdateCartShouldUpdateTheItemWhenTheFoodIsAlreadyInTheCartAndPiecesIsPositive() {
        // GIVEN
        Customer customer = createCustomerWithCart(createOneItemCartWithTwoFideua());
        Cart expectedCart = createOneItemCartWithOneFideua();

        // WHEN
        underTest.updateCart(customer, createFideua(), 1);

        // THEN
        Cart actualCart = customer.getCart();
        assertAll("The cart has one or more incorrect values after updateCart was called with"
                        + " a customer with cart: " + customer.getCart() + " and food: Fideua!",
                () -> assertEquals(expectedCart.getOrderItems(), actualCart.getOrderItems(),
                        "The content of the cart if incorrect!"),
                () -> assertEquals(expectedCart.getPrice(), actualCart.getPrice(),
                        "The price of the cart is incorrect!")
        );
    }

    @Test
    @DisplayName("updateCart should remove the item when the Food is already in the cart and pieces is zero")
    public void testUpdateCartShouldRemoveTheItemWhenTheFoodIsAlreadyInTheCartAndPiecesIsZero() {
        // GIVEN
        Customer customer = createCustomerWithCart(createOneItemCartWithTwoFideua());
        Cart expectedCart = createEmptyCart();

        // WHEN
        underTest.updateCart(customer, createFideua(), 0);

        // THEN
        Cart actualCart = customer.getCart();
        assertAll("The cart has one or more incorrect values after updateCart was called with"
                        + " a customer with cart: " + customer.getCart() + " and food: Fideua!",
                () -> assertEquals(expectedCart.getOrderItems(), actualCart.getOrderItems(),
                        "The content of the cart if incorrect!"),
                () -> assertEquals(expectedCart.getPrice(), actualCart.getPrice(),
                        "The price of the cart is incorrect!")
        );
    }

    @Test
    @DisplayName("updateCart should throw IllegalArgumentException when the Food is not in the cart and pieces is zero")
    public void updateCartShouldThrowIllegalArgumentExceptionWhenFoodIsNotInTheCartAndPiecesIsZero() {
        // GIVEN
        Customer customer = createCustomerWithCart(createEmptyCart());

        // WHEN

        // THEN
        assertThrows(IllegalArgumentException.class, () -> underTest.updateCart(customer, createFideua(), 0),
                "updateCart did not throw IllegalArgumentException when"
                        + " the customer's cart was: " + customer.getCart()
                        + " and the food was: Fideua!");
    }

    @Test
    @DisplayName("updateCart should throw IllegalArgumentException when pieces is negative")
    public void updateCartShouldThrowIllegalArgumentExceptionWhenPiecesIsNegative() {
        // GIVEN
        Customer customer = createCustomerWithCart(createEmptyCart());

        // WHEN

        // THEN
        assertThrows(IllegalArgumentException.class, () -> underTest.updateCart(customer, createFideua(), -1),
                "updateCart did not throw IllegalArgumentException when called with -1 pieces!");
    }

    private Credentials createQuickLoginCredentials() {
        Credentials quickLoginCredentials = new Credentials();
        quickLoginCredentials.setUserName("Smith");
        quickLoginCredentials.setPassword("SmithSecret");

        return quickLoginCredentials;
    }

    private Customer createCustomerWithCart(Cart cart) {
        Customer customer = new Customer();
        customer.setName("Test customer");
        customer.setBalance(new BigDecimal(100));
        customer.setCart(cart);

        return customer;
    }

    private Cart createEmptyCart() {
        var cart = new Cart();
        cart.setOrderItems(new ArrayList<>());
        cart.setPrice(BigDecimal.ZERO);
        return cart;
    }

    private Cart createOneItemCartWithOneFideua() {
        BigDecimal price = new BigDecimal(15);
        OrderItem orderItemWithOnePiece = createOrderItemWithFideua(1, price);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItemWithOnePiece);

        return createCart(orderItems, price);
    }

    private OrderItem createOrderItemWithFideua(int pieces, BigDecimal price) {
        OrderItem testOrderItem = new OrderItem();
        testOrderItem.setFood(createFideua());
        testOrderItem.setPieces(pieces);
        testOrderItem.setPrice(price);

        return testOrderItem;
    }

    private Cart createCart(List<OrderItem> orderItemList, BigDecimal price) {
        Cart cartWithOneFood = new Cart();
        cartWithOneFood.setOrderItems(orderItemList);
        cartWithOneFood.setPrice(price);

        return cartWithOneFood;
    }

    private Food createFideua() {
        Food fideua = new Food();
        fideua.setName("Fideua");
        fideua.setCalorie(new BigDecimal(558));
        fideua.setDescription("Fideua");
        fideua.setPrice(new BigDecimal(15));

        return fideua;
    }

    private Cart createOneItemCartWithTwoFideua() {
        BigDecimal price = new BigDecimal(30);
        OrderItem orderItemWithTwoPieces = createOrderItemWithFideua(2, price);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItemWithTwoPieces);

        return createCart(orderItems, price);
    }

    private Order createOrder(Customer customer, Long id) {
        var order = new Order();
        order.setOrderId(id);
        order.setCustomerId(customer.getId());
        order.setPrice(customer.getCart().getPrice());
        order.setTimestampCreated(LocalDateTime.now());
        order.setOrderItems(customer.getCart().getOrderItems());

        return order;
    }
}
