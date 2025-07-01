package com.epam.training.food.repository;

import com.epam.training.food.domain.Credentials;
import com.epam.training.food.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

     Optional<Customer> findCustomerByUserNameAndPassword(String userName, String password);

}
