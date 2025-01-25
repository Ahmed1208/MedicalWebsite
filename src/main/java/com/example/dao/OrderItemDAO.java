package com.example.dao;

import com.example.entity.OrderItem;
import jakarta.persistence.EntityManager;

public class OrderItemDAO {

    public void create(EntityManager em, OrderItem orderItem) {
        em.persist(orderItem); // No transaction management here
    }

    public OrderItem findById(EntityManager em, Long id) {
        return em.find(OrderItem.class, id); // No transaction management here
    }

    public void update(EntityManager em, OrderItem orderItem) {
        em.merge(orderItem); // No transaction management here
    }

    public void delete(EntityManager em, Long id) {
        OrderItem orderItem = findById(em, id);
        if (orderItem != null) {
            em.remove(orderItem); // No transaction management here
        }
    }
}