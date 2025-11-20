package org.choreo.gateway;

import java.math.BigDecimal;

public interface PaymentGateway {

    boolean charge(String customerId, BigDecimal amount);

    boolean refund(String paymentId);
}