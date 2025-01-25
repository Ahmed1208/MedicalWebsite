package com.example.dao;

import com.example.entity.Customer;
import jakarta.persistence.EntityManager;
import java.util.List;

public class CustomerDAO {

    // Create a new customer
    public void create(EntityManager em, Customer customer) {
        em.persist(customer); // No transaction management here
    }

    // Find a customer by ID
    public Customer findById(EntityManager em, Long id) {
        return em.find(Customer.class, id); // No transaction management here
    }

    // Find all customers
    public List<Customer> findAll(EntityManager em) {
        return em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList(); // No transaction management here
    }

    // Update a customer
    public void update(EntityManager em, Customer customer) {
        Customer existingCustomer = em.find(Customer.class, customer.getId());
        if (existingCustomer == null) {
            throw new IllegalArgumentException("Customer with ID " + customer.getId() + " does not exist.");
        }
        em.merge(customer); // No transaction management here
    }

    // Delete a customer
    public void delete(EntityManager em, Long id) {
        Customer customer = findById(em, id);
        if (customer != null) {
            em.remove(customer); // No transaction management here
        }
    }
}