package com.example;

import com.example.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class HibernateTest {

    public static void main(String[] args) {
        // Create EntityManagerFactory and EntityManager
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("medicalwebsite");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Customer customer = entityManager.find(Customer.class,4);

        //System.out.println(customer.getEmail());
    }
}