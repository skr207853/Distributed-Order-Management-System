package org.choreo.exceptionhandling.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String productId) {
        super("Product not found with ID: " + productId);
    }

    public ProductNotFoundException(String productId, String message) {
        super(message + " (ID: " + productId + ")");
    }
}
