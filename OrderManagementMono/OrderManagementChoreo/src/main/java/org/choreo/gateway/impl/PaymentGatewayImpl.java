package org.choreo.gateway.impl;

import org.choreo.gateway.PaymentGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Component
public class PaymentGatewayImpl implements PaymentGateway {

    private static final Logger log = LoggerFactory.getLogger(PaymentGatewayImpl.class);
    private static final BigDecimal MAX_SINGLE_CHARGE = new BigDecimal("10000.00");
    private static final Set<String> BLOCKLISTED_CUSTOMERS = Set.of("fraud-001", "fraud-002");

    @Override
    public boolean charge(String customerId, BigDecimal amount) {
        if (customerId == null || customerId.isBlank()) {
            log.warn("Charge rejected: customer id is blank");
            return false;
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Charge rejected for customer {}: invalid amount {}", customerId, amount);
            return false;
        }

        if (BLOCKLISTED_CUSTOMERS.contains(customerId)) {
            log.warn("Charge rejected for customer {}: customer is blocklisted", customerId);
            return false;
        }

        BigDecimal normalizedAmount = amount.setScale(2, RoundingMode.HALF_UP);

        if (normalizedAmount.compareTo(MAX_SINGLE_CHARGE) > 0) {
            log.warn("Charge rejected for customer {}: amount {} exceeds threshold {}",
                    customerId, normalizedAmount, MAX_SINGLE_CHARGE);
            return false;
        }

        log.info("Charge approved: customer {} charged {}", customerId, normalizedAmount);
        return true;
    }

    @Override
    public boolean refund(String paymentId) {
        if (paymentId == null || paymentId.isBlank()) {
            log.warn("Refund rejected: payment id is blank");
            return false;
        }

        log.info("Refund processed for payment {}", paymentId);
        return true;
    }
}
