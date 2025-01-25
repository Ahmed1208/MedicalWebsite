package com.example.rest;

import com.example.dto.CustomerDTO;
import com.example.entity.Customer;
import com.example.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private final CustomerService customerService = new CustomerService();

    private final ObjectMapper objectMapper;

    // Constructor to configure ObjectMapper
    public CustomerResource() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule for LocalDate
    }

    // Create a new customer
    @POST
    public Response createCustomer(Customer customer) {
        customerService.createCustomer(customer);
        return Response.status(Response.Status.CREATED).build();
    }

    // Get a customer by ID
    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Map Customer to CustomerDTO
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setUsername(customer.getUsername());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setBirthdate(customer.getBirthdate());
        customerDTO.setGender(customer.getGender());

        try {
            String json = objectMapper.writeValueAsString(customerDTO);
            return Response.ok(json).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Get all customers
    @GET
    public Response getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();

        // Map each Customer to CustomerDTO
        List<CustomerDTO> customerDTOs = customers.stream()
                .map(customer -> {
                    CustomerDTO dto = new CustomerDTO();
                    dto.setId(customer.getId());
                    dto.setFirstName(customer.getFirstName());
                    dto.setLastName(customer.getLastName());
                    dto.setUsername(customer.getUsername());
                    dto.setEmail(customer.getEmail());
                    dto.setBirthdate(customer.getBirthdate());
                    dto.setGender(customer.getGender());
                    return dto;
                })
                .collect(Collectors.toList());

        try {
            String json = objectMapper.writeValueAsString(customerDTOs);
            return Response.ok(json).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // Update a customer
    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") Long id, Customer customer) {
        try {
            customerService.updateCustomer(id, customer);
            return Response.ok().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    // Delete a customer
    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") Long id) {
        customerService.deleteCustomer(id);
        return Response.noContent().build();
    }

    // Close the service
    @PreDestroy
    public void close() {
        customerService.close();
    }
}