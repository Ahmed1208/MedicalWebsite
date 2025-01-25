package com.example.rest;

import com.example.dto.CustomerDTO;
import com.example.dto.OrderDTO;
import com.example.dto.OrderItemDTO;
import com.example.entity.Order;
import com.example.entity.OrderItem;
import com.example.service.OrderService;
import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Order order = orderService.getOrderByIdWithOrderItems(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Order not found").build();
        }

        // Map Order to OrderDTO
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setDate(order.getDate());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setNumberOfProducts(order.getNumberOfProducts());

        // Map Customer to CustomerDTO
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(order.getCustomer().getId());
        customerDTO.setFirstName(order.getCustomer().getFirstName());
        customerDTO.setLastName(order.getCustomer().getLastName());
        customerDTO.setUsername(order.getCustomer().getUsername());
        customerDTO.setEmail(order.getCustomer().getEmail());
        customerDTO.setBirthdate(order.getCustomer().getBirthdate());
        customerDTO.setGender(order.getCustomer().getGender());

        orderDTO.setCustomer(customerDTO);

        // Map OrderItems to OrderItemDTOs
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(this::toOrderItemDTO)
                .collect(Collectors.toList());

        orderDTO.setOrderItems(orderItemDTOs);

        return Response.ok(orderDTO).build();
    }

    private OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setCustomerId(orderItem.getCustomer().getId()); // Map customer ID
        orderItemDTO.setProductId(orderItem.getProduct().getId());   // Map product ID
        orderItemDTO.setOrderId(orderItem.getOrder().getId());       // Map order ID
        orderItemDTO.setPrice(orderItem.getPrice());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setTotalPrice(orderItem.getTotalPrice());
        return orderItemDTO;
    }

    @GET
    @Path("/customer/{customerId}")
    public Response getOrdersByCustomerId(@PathParam("customerId") Long customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);

        // Map each Order to OrderDTO
        List<OrderDTO> orderDTOs = orders.stream()
                .map(order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setId(order.getId());
                    orderDTO.setDate(order.getDate());
                    orderDTO.setTotalPrice(order.getTotalPrice());
                    orderDTO.setNumberOfProducts(order.getNumberOfProducts());

                    // Map Customer to CustomerDTO
                    CustomerDTO customerDTO = new CustomerDTO();
                    customerDTO.setId(order.getCustomer().getId());
                    customerDTO.setFirstName(order.getCustomer().getFirstName());
                    customerDTO.setLastName(order.getCustomer().getLastName());
                    customerDTO.setUsername(order.getCustomer().getUsername());
                    customerDTO.setEmail(order.getCustomer().getEmail());
                    customerDTO.setBirthdate(order.getCustomer().getBirthdate());
                    customerDTO.setGender(order.getCustomer().getGender());

                    orderDTO.setCustomer(customerDTO);

                    // Map OrderItems to OrderItemDTOs
                    List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                            .map(this::toOrderItemDTO)
                            .collect(Collectors.toList());

                    orderDTO.setOrderItems(orderItemDTOs);

                    return orderDTO;
                })
                .collect(Collectors.toList());

        return Response.ok(orderDTOs).build();
    }

    @GET
    public Response getAllOrders() {
        List<Order> orders = orderService.getAllOrdersWithOrderItems();

        // Map each Order to OrderDTO
        List<OrderDTO> orderDTOs = orders.stream()
                .map(order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setId(order.getId());
                    orderDTO.setDate(order.getDate());
                    orderDTO.setTotalPrice(order.getTotalPrice());
                    orderDTO.setNumberOfProducts(order.getNumberOfProducts());

                    // Map Customer to CustomerDTO
                    CustomerDTO customerDTO = new CustomerDTO();
                    customerDTO.setId(order.getCustomer().getId());
                    customerDTO.setFirstName(order.getCustomer().getFirstName());
                    customerDTO.setLastName(order.getCustomer().getLastName());
                    customerDTO.setUsername(order.getCustomer().getUsername());
                    customerDTO.setEmail(order.getCustomer().getEmail());
                    customerDTO.setBirthdate(order.getCustomer().getBirthdate());
                    customerDTO.setGender(order.getCustomer().getGender());

                    orderDTO.setCustomer(customerDTO);

                    // Map OrderItems to OrderItemDTOs
                    List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                            .map(this::toOrderItemDTO)
                            .collect(Collectors.toList());

                    orderDTO.setOrderItems(orderItemDTOs);

                    return orderDTO;
                })
                .collect(Collectors.toList());

        return Response.ok(orderDTOs).build();
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
    @Path("/{id}")
    public Response updateOrder(@PathParam("id") Long id, Map<String, Object> orderData) {
        try {
            // Extract the customerId
            Long customerId = Long.valueOf(orderData.get("customerId").toString());

            // Extract the productQuantities
            Map<String, Integer> productQuantities = (Map<String, Integer>) orderData.get("productQuantities");

            // Call the service method to update the order
            orderService.updateOrder(id, customerId, productQuantities);

            return Response.ok("Order updated successfully").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update order").build();
        }
    }

    @PreDestroy
    public void close() {
        orderService.close();
    }
}
