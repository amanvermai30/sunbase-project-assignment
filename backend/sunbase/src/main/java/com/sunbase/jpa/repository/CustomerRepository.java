package com.sunbase.jpa.repository;

import com.sunbase.jpa.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,String> {
    Optional<Customer> findByEmail(String email);
}
