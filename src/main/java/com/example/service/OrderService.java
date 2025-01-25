package com.example.service;

import com.example.dao.*;
import com.example.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class OrderService {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("medicalwebsite");
    private OrderDAO orderDAO = new OrderDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private ProductDAO productDAO = new ProductDAO();
    private OrderItemDAO orderItemDAO = new OrderItemDAO();

    public void createOrder(Long customerId, Map<String, Integer> productQuantities) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();

            // Validate input
            if (productQuantities == null) {
                throw new IllegalArgumentException("Product quantities cannot be null.");
            }

            if (productQuantities.isEmpty()) {
                throw new IllegalArgumentException("Product quantities cannot be empty.");
            }

            // Validate the customer
            Customer customer = customerDAO.findById(em, customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Customer with ID " + customerId + " does not exist.");
            }

            // Initialize the order
            Order order = new Order();
            order.setCustomer(customer);
            order.setDate(LocalDate.now());
            order.setTotalPrice(BigDecimal.ZERO); // Initialize total price
            order.setNumberOfProducts(0); // Initialize total products

            // Save the order to generate an ID
            orderDAO.create(em, order);
            System.out.println("Order created with ID: " + order.getId());

            BigDecimal totalPrice = BigDecimal.ZERO;
            int totalProducts = 0;

            // Process the products and quantities
            for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                Long productId = Long.valueOf(entry.getKey());
                Integer quantity = entry.getValue();

                if (productId == null || quantity == null) {
                    System.out.println("Warning: Null product ID or quantity found. Skipping this entry.");
                    continue; // Skip this entry
                }

                System.out.println("Processing product ID: " + productId + ", Quantity: " + quantity);

                // Validate the product
                Product product = productDAO.findById(em, productId);
                if (product == null) {
                    throw new IllegalArgumentException("Product with ID " + productId + " does not exist.");
                }

                if (product.getQuantity() < quantity) {
                    throw new IllegalArgumentException("Insufficient quantity for product: " + product.getName());
                }

                // Deduct product quantity from inventory
                product.setQuantity(product.getQuantity() - quantity);
                productDAO.update(em, product);
                System.out.println("Updated product quantity for ID: " + productId);

                // Create an order item
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order); // Set the order (with the generated ID)
                orderItem.setCustomer(customer);
                orderItem.setProduct(product);
                orderItem.setPrice(product.getPrice());
                orderItem.setQuantity(quantity);
                orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

                // Save the order item
                orderItemDAO.create(em, orderItem);
                System.out.println("Order item created for product ID: " + productId);

                // Update totals
                totalPrice = totalPrice.add(orderItem.getTotalPrice());
                totalProducts += quantity;
            }

            // Update the order with final totals
            order.setTotalPrice(totalPrice);
            order.setNumberOfProducts(totalProducts);
            orderDAO.update(em, order); // Update the order in the database
            System.out.println("Order updated with total price: " + totalPrice + ", Total products: " + totalProducts);

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            // Roll back the transaction in case of an exception
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e; // Re-throw the exception
        } finally {
            // Close the EntityManager
            if (em != null) {
                em.close();
            }
        }
    }

    // Find an order by ID
    public Order getOrderById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return orderDAO.findById(em, id);
        } finally {
            em.close();
        }
    }

    // Find all orders
    public List<Order> getAllOrders() {
        EntityManager em = emf.createEntityManager();
        try {
            return orderDAO.findAll(em);
        } finally {
            em.close();
        }
    }

    public Order getOrderByIdWithOrderItems(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return orderDAO.findByIdWithOrderItems(em, id);
        } finally {
            em.close();
        }
    }

    public List<Order> getAllOrdersWithOrderItems() {
        EntityManager em = emf.createEntityManager();
        try {
            return orderDAO.findAllWithOrderItems(em);
        } finally {
            em.close();
        }
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        EntityManager em = emf.createEntityManager();
        try {
            return orderDAO.findByCustomerIdWithOrderItems(em, customerId);
        } finally {
            em.close();
        }
    }

    // Update an order
    public void updateOrder(Long orderId, Long customerId, Map<String, Integer> productQuantities) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();

            // Find the existing order
            Order existingOrder = orderDAO.findById(em, orderId);
            if (existingOrder == null) {
                throw new NotFoundException("Order with ID " + orderId + " not found.");
            }

            // Validate the customer
            Customer customer = customerDAO.findById(em, customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Customer with ID " + customerId + " does not exist.");
            }

            // Update the customer for the order
            existingOrder.setCustomer(customer);

            // Clear existing order items
            existingOrder.getOrderItems().clear();

            BigDecimal totalPrice = BigDecimal.ZERO;
            int totalProducts = 0;

            // Process the products and quantities
            for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                Long productId = Long.valueOf(entry.getKey());
                Integer quantity = entry.getValue();

                if (productId == null || quantity == null) {
                    System.out.println("Warning: Null product ID or quantity found. Skipping this entry.");
                    continue; // Skip this entry
                }

                System.out.println("Processing product ID: " + productId + ", Quantity: " + quantity);

                // Validate the product
                Product product = productDAO.findById(em, productId);
                if (product == null) {
                    throw new IllegalArgumentException("Product with ID " + productId + " does not exist.");
                }

                if (product.getQuantity() < quantity) {
                    throw new IllegalArgumentException("Insufficient quantity for product: " + product.getName());
                }

                // Deduct product quantity from inventory
                product.setQuantity(product.getQuantity() - quantity);
                productDAO.update(em, product);
                System.out.println("Updated product quantity for ID: " + productId);

                // Create an order item
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(existingOrder); // Set the order
                orderItem.setCustomer(customer);
                orderItem.setProduct(product);
                orderItem.setPrice(product.getPrice());
                orderItem.setQuantity(quantity);
                orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

                // Save the order item
                orderItemDAO.create(em, orderItem);
                System.out.println("Order item created for product ID: " + productId);

                // Update totals
                totalPrice = totalPrice.add(orderItem.getTotalPrice());
                totalProducts += quantity;
            }

            // Update the order with final totals
            existingOrder.setTotalPrice(totalPrice);
            existingOrder.setNumberOfProducts(totalProducts);
            orderDAO.update(em, existingOrder); // Update the order in the database
            System.out.println("Order updated with total price: " + totalPrice + ", Total products: " + totalProducts);

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            // Roll back the transaction in case of an exception
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e; // Re-throw the exception
        } finally {
            // Close the EntityManager
            if (em != null) {
                em.close();
            }
        }
    }

    // Delete an order
    public void deleteOrder(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin();

            // Use the OrderDAO to delete the order
            orderDAO.delete(em, id);

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