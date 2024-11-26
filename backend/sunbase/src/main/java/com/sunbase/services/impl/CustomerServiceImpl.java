package com.sunbase.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunbase.dtos.CustomerRequest;
import com.sunbase.dtos.CustomerResponse;
import com.sunbase.exceptions.CustomerException;
import com.sunbase.jpa.model.Customer;
import com.sunbase.jpa.repository.CustomerRepository;
import com.sunbase.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private final ModelMapper modelMapper = new ModelMapper();


    @Override
    public CustomerResponse saveCustomer(CustomerRequest request) {

        /* here: if customer is already present then update it */
        if(request.getId() != null) {
            Optional<Customer> optionalCustomer = customerRepository.findById(request.getId());
            if (optionalCustomer.isEmpty()){
                return null;
            }
            Customer customer = optionalCustomer.get();
            setCustomer(customer,request);
            Customer savedCustomer = customerRepository.save(customer);
            return modelMapper.map(savedCustomer,CustomerResponse.class);

        }
        /* here: if customer is new then we create it */
        Customer customer = new Customer();
        customer.setUuid(UUID.randomUUID().toString());
        setCustomer(customer,request);
        customerRepository.save(customer);
        return modelMapper.map(customer,CustomerResponse.class);
    }

    @Override
    public CustomerResponse getCustomerById(String uuid) {
       Optional<Customer> optionalCustomer =  customerRepository.findById(uuid);
       if(optionalCustomer.isEmpty()){
           return null; /* here: if customer is not present we throw error */
       }
       Customer customer = optionalCustomer.get();
       return modelMapper.map(customer,CustomerResponse.class);
    }

    @Override
    public Optional<Customer> getCustomerByEmail(String email) {

        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer getLoggedInCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            Optional<Customer> loggedInCustomer = customerRepository.findByEmail(userDetails.getUsername());

            if (loggedInCustomer.isPresent()) {
                return loggedInCustomer.get();
            }
        }

        throw new RuntimeException("Unauthorized access");
    }

    @Override
    public CustomerResponse deleteCustomer(String uuid) {
        Optional<Customer> optionalCustomer =  customerRepository.findById(uuid);
        if(optionalCustomer.isEmpty()){
           throw new CustomerException("customer not found."); /* here: if customer is not present we throw error */
        }

        Customer loggedInCustomer = getLoggedInCustomer();
        Customer customer = optionalCustomer.get();
        if (loggedInCustomer.getUuid().equals(customer.getUuid())) {
            throw new CustomerException("Logged in customer cannot be deleted");
        }
        customerRepository.delete(customer);
        return modelMapper.map(customer,CustomerResponse.class);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {

        List<Customer> customerList = customerRepository.findAll();
        return customerList.stream().map(customer -> modelMapper.map(customer,CustomerResponse.class)).toList();
    }


    public List<CustomerResponse> saveOrUpdateInBulk(List<Customer> customers) {
        return List.of();
    }

    @Override
    public List<CustomerResponse> fetchDataFromServer() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list", HttpMethod.GET, entity, String.class);

        System.out.println(response + "responsesss");

        List<Map<String, Object>> rawList = objectMapper.readValue(response.getBody(), new TypeReference<>() {});

        List<Customer> customers = rawList.stream().map(map -> {
            Customer customer = new Customer();
            customer.setUuid(Objects.toString(map.get("uuid"), null));
            customer.setEmail(Objects.toString(map.get("email"), null));
            customer.setFirstName(Objects.toString(map.get("first_name"), null));
            customer.setLastName(Objects.toString(map.get("last_name"), null));
            customer.setPhone(Objects.toString(map.get("phone"), null));
            customer.setStreet(Objects.toString(map.get("street"), null));
            customer.setAddress(Objects.toString(map.get("address"), null));
            customer.setCity(Objects.toString(map.get("city"), null));
            customer.setState(Objects.toString(map.get("state"), null));
            return customer;

        }).collect(Collectors.toList());

        List<Customer> existingCustomers = customerRepository.findAll();
        existingCustomers.remove(getLoggedInCustomer());

        for (Customer newCustomer : customers) {
            boolean exists = existingCustomers.stream()
                    .anyMatch(existingCustomer -> twoCustomersAreEqual(newCustomer, existingCustomer));

            if (!exists) {
                customerRepository.save(newCustomer);
            }
        }
        customerRepository.saveAll(customers);
        return customers.stream().map(customer -> modelMapper.map(customer,CustomerResponse.class)).toList();
    }


    private String getAccessToken() {
        String url = "https://qa.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

        Map<String, String> requestData = new HashMap<>();
        requestData.put("login_id", "test@sunbasedata.com");
        requestData.put("password", "Test@123");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestData, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return objectMapper.readTree(response.getBody()).path("access_token").asText();

            } else {
                throw new RuntimeException("HTTP error! Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw  new CustomerException("Error retrieving access token");
        }
    }

    public void setCustomer(Customer customer,CustomerRequest request){
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setStreet(request.getStreet());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
    }

    private boolean twoCustomersAreEqual(Customer c1, Customer c2) {
        return Objects.equals(c1.getFirstName(), c2.getFirstName()) &&
                Objects.equals(c1.getLastName(), c2.getLastName()) &&
                Objects.equals(c1.getEmail(), c2.getEmail()) &&
                Objects.equals(c1.getPhone(), c2.getPhone()) &&
                Objects.equals(c1.getAddress(), c2.getAddress()) &&
                Objects.equals(c1.getCity(), c2.getCity()) &&
                Objects.equals(c1.getState(), c2.getState()) &&
                Objects.equals(c1.getStreet(), c2.getStreet());
    }

}
