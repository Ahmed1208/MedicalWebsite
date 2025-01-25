package com.example.dao;

import com.example.entity.Product;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ProductDAO {

    // Create a new product
    public void create(EntityManager em, Product product) {
        em.persist(product); // No transaction management here
    }

    // Find a product by ID
    public Product findById(EntityManager em, Long id) {
        return em.find(Product.class, id); // No transaction management here
    }

    // Find all products
    public List<Product> findAll(EntityManager em) {
        return em.createQuery("SELECT p FROM Product p", Product.class).getResultList(); // No transaction management here
    }

    // Update a product
    public void update(EntityManager em, Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (product.getId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null.");
        }

        Product existingProduct = em.find(Product.class, product.getId());
        if (existingProduct == null) {
            throw new IllegalArgumentException("Product with ID " + product.getId() + " does not exist.");
        }

        // Merge the product changes
        em.merge(product); // No transaction management here
    }

    // Delete a product
    public void delete(EntityManager em, Long id) {
        Product product = findById(em, id);
        if (product != null) {
            em.remove(product); // No transaction management here
        }
    }
}