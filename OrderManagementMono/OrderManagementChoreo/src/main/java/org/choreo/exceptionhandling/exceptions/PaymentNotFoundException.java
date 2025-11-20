package org.choreo.exceptionhandling.exceptions;

public class PaymentNotFoundException extends RuntimeException{
    public PaymentNotFoundException(String orderId) {
        super("Payment not found for order: " + orderId);
    }

}
