package com.example.rest;

import com.example.entity.Order;
import com.example.service.OrderService;
import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private OrderService orderService = new OrderService();

    // Create a new order
    @POST
    public Response createOrder(Map<String, Object> orderData) {
        try {
            // Extract the customerId
            Long customerId = Long.valueOf(orderData.get("customerId").toString());

            // Extract the productQuantities
            Map<String, Integer> productQuantities = (Map<String, Integer>) orderData.get("productQuantities");

            // Call the service method
            orderService.createOrder(customerId, productQuantities);

            return Response.status(Response.Status.CREATED).entity("Order created successfully").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while creating the order").build();
        }
    }


    // Retrieve an order by ID
    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Order not found").build();
        }
        return Response.ok(order).build();
    }

    // List all orders
    @GET
    public Response getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return Response.ok(orders).build();
    }

    // Delete an order by ID
    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") Long id) {
        try {
            orderService.deleteOrder(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete order").build();
        }
    }

    // Update an order
    @PUT
    public Response updateOrder(Order updatedOrder) {
        try {
            orderService.updateOrder(updatedOrder);
            return Response.ok("Order updated successfully").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update order").build();
        }
    }

    @PreDestroy
    public void close() {
        orderService.close();
    }
}
