package com.example.service;

import com.example.dao.CustomerDAO;
import com.example.entity.Customer;
import com.example.entity.Customer.Gender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

public class CustomerService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("medicalwebsite");
    private CustomerDAO customerDAO = new CustomerDAO();

    // Create a new customer
    public void createCustomer(Customer customer) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();

            // Use the CustomerDAO to create the customer
            customerDAO.create(em, customer);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e; // Re-throw the exception
        } finally {
            em.close();
        }
    }

    // Get a customer by ID
    public Customer getCustomerById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            // Use the CustomerDAO to find the customer
            Customer customer = customerDAO.findById(em, id);

            if (customer == null) {
                return null;
            }

            return customer;
        } finally {
            em.close();
        }
    }

    public List<Customer> getAllCustomers() {
        EntityManager em = emf.createEntityManager();
        try {
            return customerDAO.findAll(em);
        } finally {
            em.close();
        }
    }

    // Update a customer
    public void updateCustomer(Long id, Customer updatedCustomer) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();

            // Find the existing customer
            Customer existingCustomer = customerDAO.findById(em, id);
            if (existingCustomer == null) {
                throw new NotFoundException("Customer with ID " + id + " not found.");
            }

            // Update only the fields that are not null in the updatedCustomer object
            if (updatedCustomer.getFirstName() != null) {
                existingCustomer.setFirstName(updatedCustomer.getFirstName());
            }
            if (updatedCustomer.getLastName() != null) {
                existingCustomer.setLastName(updatedCustomer.getLastName());
            }
            if (updatedCustomer.getUsername() != null) {
                existingCustomer.setUsername(updatedCustomer.getUsername());
            }
            if (updatedCustomer.getEmail() != null) {
                existingCustomer.setEmail(updatedCustomer.getEmail());
            }
            if (updatedCustomer.getPassword() != null) {
                existingCustomer.setPassword(updatedCustomer.getPassword());
            }
            if (updatedCustomer.getBirthdate() != null) {
                existingCustomer.setBirthdate(updatedCustomer.getBirthdate());
            }
            if (updatedCustomer.getGender() != null) {
                existingCustomer.setGender(updatedCustomer.getGender());
            }

            // Save the updated customer
            customerDAO.update(em, existingCustomer);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e; // Re-throw the exception
        } finally {
            em.close();
        }
    }

    // Delete a customer
    public void deleteCustomer(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();

            // Use the CustomerDAO to delete the customer
            customerDAO.delete(em, id);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e; // Re-throw the exception
        } finally {
            em.close();
        }
    }

    // Check if a customer is valid
    public boolean isValidCustomer(Long customerId) {
        EntityManager em = emf.createEntityManager();
        try {
            // Use the CustomerDAO to find the customer
            Customer customer = customerDAO.findById(em, customerId);
            return customer != null;
        } finally {
            em.close();
        }
    }

    // Close the EntityManagerFactory
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}