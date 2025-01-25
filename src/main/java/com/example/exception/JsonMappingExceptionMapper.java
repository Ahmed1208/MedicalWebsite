package com.example.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {

    @Override
    public Response toResponse(JsonMappingException exception) {
        String errorMessage = "Invalid value provided for a field. Please check your input and try again.";
        if (exception.getPath() != null && !exception.getPath().isEmpty()) {
            String field = exception.getPath().get(0).getFieldName();
            if (field != null) {
                errorMessage = "Invalid value for field '" + field + "'. Please provide a valid value.";
            }
        }
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(errorMessage)
                       .build();
    }
}
