package ru.mentee.power.crm.domain;

public record Contact(String email, String phone, Address address) {
    public Contact {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        if (phone == null) {
            throw new IllegalArgumentException("Phone cannot be null");
        }

        if (address == null) {
            throw new IllegalArgumentException("Address cannot be null");
        }
    }
}