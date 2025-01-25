package com.example.service;

import com.example.dao.ProductDAO;
import com.example.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;

public class ProductService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("medicalwebsite");
    private ProductDAO productDAO = new ProductDAO();

    // Create a new product
    public void createProduct(Product product) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();

            // Use the ProductDAO to create the product
            productDAO.create(em, product);

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

    // Find a product by ID
    public Product getProductById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            // Use the ProductDAO to find the product
            return productDAO.findById(em, id);
        } finally {
            em.close();
        }
    }

    // Get all products
    public List<Product> getAllProducts() {
        EntityManager em = emf.createEntityManager();
        try {
            // Use the ProductDAO to find all products
            return productDAO.findAll(em);
        } finally {
            em.close();
        }
    }

    // Update a product
    public void updateProduct(Long id, Product updatedProduct) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();

            // Find the existing product
            Product existingProduct = productDAO.findById(em, id);
            if (existingProduct == null) {
                throw new IllegalArgumentException("Product with ID " + id + " not found.");
            }

            // Only update fields that are not null
            if (updatedProduct.getName() != null) {
                existingProduct.setName(updatedProduct.getName());
            }
            if (updatedProduct.getDescription() != null) {
                existingProduct.setDescription(updatedProduct.getDescription());
            }
            if (updatedProduct.getPrice() != null) {
                existingProduct.setPrice(updatedProduct.getPrice());
            }
            if (updatedProduct.getQuantity() != null) {
                existingProduct.setQuantity(updatedProduct.getQuantity());
            }

            // Use the ProductDAO to update the product
            productDAO.update(em, existingProduct);

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

    // Delete a product
    public void deleteProduct(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();

            // Use the ProductDAO to delete the product
            productDAO.delete(em, id);

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

    // Close the EntityManagerFactory
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}