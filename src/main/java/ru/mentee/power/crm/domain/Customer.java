package ru.mentee.power.crm.domain;

import java.util.UUID;

public record Customer(
    UUID id,
    Contact contact,
    Address billingAddress,
    String loyaltyTier
) {
    public Customer {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        if (billingAddress == null) {
            throw new IllegalArgumentException("BillingAddress cannot be null");
        }
        if (!loyaltyTier.equals("L1") && !loyaltyTier.equals("L2") && !loyaltyTier.equals("L3")) {
            throw new IllegalArgumentException("Invalid loyaltyTier");
        }
    }
}