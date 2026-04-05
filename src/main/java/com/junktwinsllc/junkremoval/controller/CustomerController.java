package com.junktwinsllc.junkremoval.controller;

import com.junktwinsllc.junkremoval.dto.CustomerDTO;
import com.junktwinsllc.junkremoval.model.Customer;
import com.junktwinsllc.junkremoval.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomerDTO.from(customerService.createCustomer(customer)));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getCustomers(@RequestParam(required = false) String name) {
        List<Customer> results = (name != null && !name.isBlank())
                ? customerService.searchByName(name)
                : customerService.getAllCustomers();
        return ResponseEntity.ok(results.stream().map(CustomerDTO::from).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(CustomerDTO.from(customerService.getCustomerById(id)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(CustomerDTO.from(customerService.getByEmail(email)));
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerDTO> getByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(CustomerDTO.from(customerService.getByPhone(phone)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return ResponseEntity.ok(CustomerDTO.from(customerService.updateCustomer(id, customer)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}