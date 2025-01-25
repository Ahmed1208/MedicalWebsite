package com.example.rest;

import com.example.dao.ProductDTO;
import com.example.entity.Product;
import com.example.service.ProductService;
import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    private ProductService productService = new ProductService();

    // Create a new product
    @POST
    public Response createProduct(Product product) {
        productService.createProduct(product);
        return Response.status(Response.Status.CREATED).build();
    }

    // Get a product by ID
    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Product with ID " + id + " not found.").build();
        }

        // Map Product to ProductDTO
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());

        return Response.ok(productDTO).build();
    }

    // Get all products
    @GET
    public Response getAllProducts() {
        List<Product> products = productService.getAllProducts();

        // Map each Product to ProductDTO
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setDescription(product.getDescription());
                    dto.setPrice(product.getPrice());
                    dto.setQuantity(product.getQuantity());
                    return dto;
                })
                .collect(Collectors.toList());

        return Response.ok(productDTOs).build();
    }

    // Update a product
    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, Product product) {
        try {
            productService.updateProduct(id, product);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    // Delete a product
    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        productService.deleteProduct(id);
        return Response.noContent().build();
    }

    // Close the service
    @PreDestroy
    public void close() {
        productService.close();
    }
}
