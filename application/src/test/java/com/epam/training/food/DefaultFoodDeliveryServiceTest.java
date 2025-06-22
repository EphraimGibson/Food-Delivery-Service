package com.epam.training.food;

import com.epam.training.food.data.FileDataStore;
import com.epam.training.food.domain.Credentials;
import com.epam.training.food.domain.Customer;
import com.epam.training.food.service.DefaultFoodDeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SpringApplicationStarter.class, properties = "baseDirPath=./../test/")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DefaultFoodDeliveryServiceTest {
    private static final long NEXT_ORDER_ID = 0L;

    /*
      If the Application class is added to the application context as bean, it prevents the context to stop.
      Mocking the bean solves the problem.
     */
    @MockBean
    private Application application;

    @Autowired
    private FileDataStore fileDataStore;

    @Autowired
    private DefaultFoodDeliveryService underTest;

    @BeforeEach
    public void setup() {
        fileDataStore.init();
        assertAll("Some of the collections in FileDataStore are not initialized correctly.",
                () -> assertFalse(fileDataStore.getFoods().isEmpty(),
                        "Foods collection in FileDataStore is empty after FileDataStore.init() call"),
                () -> assertFalse(fileDataStore.getCustomers().isEmpty(),
                        "Customers collection in FileDataStore is empty after FileDataStore.init() call")
        );
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


    private Credentials createQuickLoginCredentials() {
        Credentials quickLoginCredentials = new Credentials();
        quickLoginCredentials.setUserName("Smith");
        quickLoginCredentials.setPassword("SmithSecret");

        return quickLoginCredentials;
    }

}
