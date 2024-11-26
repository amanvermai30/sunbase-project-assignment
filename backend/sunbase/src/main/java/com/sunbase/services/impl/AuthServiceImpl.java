package com.sunbase.services.impl;

import com.sunbase.dtos.CustomerRequest;
import com.sunbase.exceptions.CustomerException;
import com.sunbase.jpa.model.Customer;
import com.sunbase.jpa.repository.CustomerRepository;
import com.sunbase.security.jwt.JwtUtility;
import com.sunbase.services.AuthService;
import com.sunbase.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtility jwtUtility;


    @Override
    public String register(CustomerRequest request) {
        if (request.getEmail() != null && !request.getEmail().isEmpty() && customerService.getCustomerByEmail(request.getEmail()).isPresent())
            throw new CustomerException("Account already exists");

        Customer customer = new Customer();
        customer.setUuid(UUID.randomUUID().toString());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customerRepository.save(customer);
        return  "Registration Successful";
    }

    @Override
    public String login(CustomerRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new CustomerException("Unauthorized , password is incorrect ");
        }

        if (!userDetails.isAccountNonExpired()) {
            throw new CustomerException("Account is expired !");
        }
        if (!userDetails.isCredentialsNonExpired()) {
            throw new CustomerException("Account credentials are expired !");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword(), userDetails.getAuthorities());
        authenticationManager.authenticate(authentication);

        return jwtUtility.generateToken(userDetails.getUsername());
    }
}
