package com.junktwinsllc.junkremoval.service;

import com.junktwinsllc.junkremoval.model.Customer;
import com.junktwinsllc.junkremoval.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        if (customer.getEmail() != null) {
            customerRepository.findByEmail(customer.getEmail()).ifPresent(c -> {
                throw new RuntimeException("Email already exists: " + customer.getEmail());
            });
        }
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
    }

    public List<Customer> searchByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }

    public Customer getByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + email));
    }

    public Customer getByPhone(String phone) {
        return customerRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + phone));
    }

    public Customer updateCustomer(Long id, Customer updated) {
        Customer existing = getCustomerById(id);
        if (updated.getName() != null)    existing.setName(updated.getName());
        if (updated.getEmail() != null)   existing.setEmail(updated.getEmail());
        if (updated.getPhone() != null)   existing.setPhone(updated.getPhone());
        if (updated.getAddress() != null) existing.setAddress(updated.getAddress());
        return customerRepository.save(existing);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found: " + id);
        }
        customerRepository.deleteById(id);
    }
}