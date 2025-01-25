package com.example.dao;

import com.example.entity.Order;
import jakarta.persistence.EntityManager;

import java.util.List;

public class OrderDAO {

    public void create(EntityManager em, Order order) {
        em.persist(order); // No transaction management here
    }

    public Order findById(EntityManager em, Long id) {
        return em.find(Order.class, id); // No transaction management here
    }

    public Order findByIdWithOrderItems(EntityManager em, Long id) {
        return em.createQuery(
                        "SELECT o FROM Order o " +
                                "LEFT JOIN FETCH o.orderItems " +
                                "WHERE o.id = :id", Order.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<Order> findAllWithOrderItems(EntityManager em) {
        return em.createQuery(
                        "SELECT DISTINCT o FROM Order o " +
                                "LEFT JOIN FETCH o.orderItems", Order.class)
                .getResultList();
    }

    public List<Order> findByCustomerIdWithOrderItems(EntityManager em, Long customerId) {
        return em.createQuery(
                        "SELECT DISTINCT o FROM Order o " +
                                "LEFT JOIN FETCH o.orderItems " +
                                "WHERE o.customer.id = :customerId", Order.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }

    public List<Order> findAll(EntityManager em) {
        return em.createQuery("SELECT o FROM Order o", Order.class).getResultList(); // No transaction management here
    }

    public void update(EntityManager em, Order order) {
        em.merge(order); // No transaction management here
    }

    public void delete(EntityManager em, Long id) {
        Order order = findById(em, id);
        if (order != null) {
            em.remove(order); // No transaction management here
        }
    }
}