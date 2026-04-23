package ru.mentee.power.crm.domain;

import java.util.UUID;

public record Lead(
    UUID id,
    Contact contact,
    String company,
    String status
) {
    public Lead {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        if (!status.equals("NEW") && !status.equals("CONTACTED") && !status.equals("QUALIFIED")) {
            throw new IllegalArgumentException("Invalid status");
        }
    }
}
